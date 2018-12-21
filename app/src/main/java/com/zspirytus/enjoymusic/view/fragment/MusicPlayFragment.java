package com.zspirytus.enjoymusic.view.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayProgressChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.impl.BlurTransformation;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnMultiEventImageViewListener;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.utils.AnimationUtil;
import com.zspirytus.enjoymusic.utils.TimeUtil;
import com.zspirytus.enjoymusic.view.widget.MultiEventImageView;

import java.io.File;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_play_layout)
public class MusicPlayFragment extends CommonHeaderBaseFragment implements View.OnClickListener, MusicPlayStateObserver,
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
                ForegroundMusicController.getInstance().playPrevious();
                break;
            case R.id.play_pause:
                boolean isPlaying = ForegroundMusicController.getInstance().isPlaying();
                Music currentPlayingMusic = ForegroundMusicCache.getInstance().getCurrentPlayingMusic();
                if (isPlaying) {
                    ForegroundMusicController.getInstance().pause();
                } else {
                    ForegroundMusicController.getInstance().play(currentPlayingMusic);
                }
                break;
            case R.id.next:
                ForegroundMusicController.getInstance().playNext();
                break;
        }
    }

    @Override
    public void onProgressChanged(final int progress) {
        getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSeekBar.setProgress(progress);
            }
        });
    }

    @Override
    public void onPlayingStateChanged(final boolean isPlaying) {
        getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setButtonSrc(isPlaying);
            }
        });
    }

    @Override
    public void onPlayedMusicChanged(final Music music) {
        getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setView(music);
            }
        });
    }

    @Override
    protected void initData() {
        mCurrentPlayingMusic = ForegroundMusicCache.getInstance().getCurrentPlayingMusic();
        mCover.setOnMultiEventImageViewListener(new OnMultiEventImageViewListener() {
            @Override
            public void onClick() {

            }

            @Override
            public void onMoveToLeft() {
                ForegroundMusicController.getInstance().playPrevious();
            }

            @Override
            public void onMoveToRight() {
                ForegroundMusicController.getInstance().playNext();
            }
        });
    }

    @Override
    protected void initView() {
        setNavIconAction(false);
        setHeaderViewColor(R.color.transparent);
        if (mCurrentPlayingMusic != null) {
            String musicAlbumUri = mCurrentPlayingMusic.getMusicThumbAlbumCoverPath();
            GlideApp.with(this).load(musicAlbumUri != null ? new File(musicAlbumUri) : R.color.grey)
                    .into(mCover);
            mTotalTime.setText(TimeUtil.convertLongToMinsSec(mCurrentPlayingMusic.getMusicDuration()));
        }
        setButtonSrc(ForegroundMusicController.getInstance().isPlaying());
        mCover.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayOrPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    @Override
    protected void registerEvent() {
        IPlayMusicChangeObserverImpl.getInstance().register(this);
        IPlayStateChangeObserverImpl.getInstance().register(this);
        IPlayProgressChangeObserverImpl.getInstance().register(this);
    }

    @Override
    protected void unregisterEvent() {
        IPlayMusicChangeObserverImpl.getInstance().unregister(this);
        IPlayStateChangeObserverImpl.getInstance().unregister(this);
        IPlayProgressChangeObserverImpl.getInstance().unregister(this);
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
            GlideApp.with(this).load(R.drawable.ic_pause_black_48dp).into(mPlayOrPauseButton);
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
        } else {
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
            GlideApp.with(this).load(R.drawable.ic_play_arrow_black_48dp).into(mPlayOrPauseButton);
            AnimationUtil.ofFloat(mPlayOrPauseButton, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
        }
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

    private void setView(Music music) {
        String musicThumbAlbumCoverPath = music.getMusicThumbAlbumCoverPath();
        if (musicThumbAlbumCoverPath != null) {
            File coverFile = new File(musicThumbAlbumCoverPath);
            GlideApp.with(this).load(coverFile).into(mCover);
        }
        mTotalTime.setText(TimeUtil.convertLongToMinsSec(music.getMusicDuration()));
        setupSeekBar(music);
        setBackgroundBlur(music);
    }

    private void setBackgroundBlur(Music music) {
        String imagePath = music.getMusicThumbAlbumCoverPath();
        File file = MusicCoverFileCache.getInstance().getCoverFile(imagePath);
        if (file != null) {
            RequestOptions options = new RequestOptions();
            options = options.transform(new BlurTransformation(getParentActivity(), 24, 3));
            GlideApp.with(getParentActivity())
                    .load(file)
                    .dontAnimate()
                    .apply(options)
                    .into(mBackground);
        }
    }

    public static MusicPlayFragment getInstance() {
        return new MusicPlayFragment();
    }

}
