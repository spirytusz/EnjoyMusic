package com.zspirytus.enjoymusic.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnMultiEventImageViewListener;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.utils.AnimationUtil;
import com.zspirytus.enjoymusic.utils.TimeUtil;
import com.zspirytus.enjoymusic.view.widget.MultiEventImageView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_play)
public class MusicPlayFragment extends BaseFragment
        implements View.OnClickListener, MusicPlayStateObserver,
        MusicPlayProgressObserver, PlayedMusicChangeObserver {

    @ViewInject(R.id.background)
    private ImageView mBackground;

    @ViewInject(R.id.cover)
    private MultiEventImageView mCover;

    @ViewInject(R.id.now_time)
    private TextView mNowTime;
    @ViewInject(R.id.music_seekbar)
    private SeekBar mSeekBar;
    @ViewInject(R.id.total_time)
    private TextView mTotalTime;

    @ViewInject(R.id.previous)
    private ImageView mPreviousButton;
    @ViewInject(R.id.play_pause)
    private ImageView mPlayOrPauseButton;
    @ViewInject(R.id.next)
    private ImageView mNextButton;

    private Music mCurrentPlayingMusic;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
                Music previousMusic = MusicPlayOrderManager.getInstance().getPreviousMusic();
                ForegroundMusicController.getInstance().play(previousMusic);
                break;
            case R.id.play_pause:
                boolean isPlaying = MediaPlayController.getInstance().isPlaying();
                Music currentPlayingMusic = CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic();
                if (isPlaying) {
                    ForegroundMusicController.getInstance().pause(currentPlayingMusic);
                } else {
                    ForegroundMusicController.getInstance().play(currentPlayingMusic);
                }
                break;
            case R.id.next:
                Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic();
                ForegroundMusicController.getInstance().play(nextMusic);
                break;
        }
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        setButtonSrc(isPlaying);
    }

    @Override
    public void onProgressChanged(int currentPlayingMills) {
        mSeekBar.setProgress(currentPlayingMills);
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        setView(music);
        setupSeekBar(music);
    }

    @Subscriber(tag = Constant.EventBusTag.MUSIC_NAME_SET)
    public void setView(Music music) {
        String musicThumbAlbumCoverPath = music.getMusicThumbAlbumCoverPath();
        if (musicThumbAlbumCoverPath != null) {
            File coverFile = new File(musicThumbAlbumCoverPath);
            Glide.with(this).load(coverFile).into(mCover);
            //loadBlurCover(coverFile);
        }
        EventBus.getDefault().post(music.getMusicName(), Constant.EventBusTag.SET_MAIN_ACTIVITY_TOOLBAR_TITLE);
        mTotalTime.setText(TimeUtil.convertLongToMinsSec(music.getMusicDuration()));
        setupSeekBar(music);
    }

    @Override
    protected void initData() {
        mCurrentPlayingMusic = CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic();
        mCover.setOnMultiEventImageViewListener(new OnMultiEventImageViewListener() {
            @Override
            public void onClick() {

            }

            @Override
            public void onMoveToLeft() {
                Music previousMusic = MusicPlayOrderManager.getInstance().getPreviousMusic();
                ForegroundMusicController.getInstance().play(previousMusic);
            }

            @Override
            public void onMoveToRight() {
                Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic();
                ForegroundMusicController.getInstance().play(nextMusic);
            }
        });
    }

    @Override
    protected void initView() {
        if (mCurrentPlayingMusic != null) {
            String musicAlbumUri = mCurrentPlayingMusic.getMusicThumbAlbumCoverPath();
            Glide.with(this).load(musicAlbumUri != null ? new File(musicAlbumUri) : R.color.grey)
                    .into(mCover);
            mTotalTime.setText(TimeUtil.convertLongToMinsSec(mCurrentPlayingMusic.getMusicDuration()));
        }
        setButtonSrc(MediaPlayController.getInstance().isPlaying());
        mCover.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayOrPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().registerMusicPlayStateObserver(this);
        MediaPlayController.getInstance().registerProgressChange(this);
        MediaPlayController.getInstance().registerPlayedMusicChangeObserver(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
        MediaPlayController.getInstance().unregisterMusicPlayStateObserver(this);
        MediaPlayController.getInstance().unregisterProgressChange(this);
        MediaPlayController.getInstance().unregisterPlayedMusicChangeObserver(this);
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
            Glide.with(this).load(R.drawable.ic_pause_black_48dp).into(mPlayOrPauseButton);
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
        } else {
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
            Glide.with(this).load(R.drawable.ic_play_arrow_black_48dp).into(mPlayOrPauseButton);
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
        }
    }

    private void loadBlurCover(File coverFile) {
        RequestOptions options = new RequestOptions()
                .dontAnimate()
                .transform(new BlurTransformation(getParentActivity(), 14, 3));
        Glide.with(this).load(coverFile)
                .apply(options)
                .into(mBackground);
    }

    private void setupSeekBar(Music music) {
        mSeekBar.setMax((int) (music.getMusicDuration() / 1000));
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int milliseconds = progress * 1000;
                String millisecondsString = TimeUtil.convertIntToMinsSec(milliseconds);
                mNowTime.setText(millisecondsString);
                if (fromUser) {
                    ForegroundMusicController.getInstance().seekTo(milliseconds);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public static MusicPlayFragment getInstance() {
        return new MusicPlayFragment();
    }

}
