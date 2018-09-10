package com.zspirytus.enjoymusic.view.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.utils.TimeUtil;

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
    @ViewInject(R.id.music_play_fragment_toolbar)
    private Toolbar mToolbar;

    @ViewInject(R.id.cover)
    private ImageView mCover;

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

    public static MusicPlayFragment getInstance() {
        MusicPlayFragment musicPlayFragment = new MusicPlayFragment();
        return musicPlayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        registerEvent();
        initData();
        initView();
        setListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        unregisterEvent();
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
                Music previousMusic = MusicPlayOrderManager.getInstance().getPreviousMusic();
                ForegroundMusicController.getInstance().play(previousMusic);
                setView(previousMusic);
                break;
            case R.id.play_pause:
                boolean isPlaying = MediaPlayController.getInstance().isPlaying();
                Music currentPlayingMusic = MusicCache.getInstance().getCurrentPlayingMusic();
                if (isPlaying) {
                    ForegroundMusicController.getInstance().pause(currentPlayingMusic);
                } else {
                    ForegroundMusicController.getInstance().play(currentPlayingMusic);
                }
                break;
            case R.id.next:
                Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic();
                ForegroundMusicController.getInstance().play(nextMusic);
                setView(nextMusic);
                break;
        }
    }

    @Override
    public void onPlayingState(boolean isPlaying) {
        setButtonSrc(isPlaying);
    }

    @Override
    public void onProgressChange(int currentPlayingMills) {
        mSeekBar.setProgress(currentPlayingMills);
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        setView(music);
        setupSeekBar(music);
    }

    @Subscriber(tag = FinalValue.EventBusTag.MUSIC_NAME_SET)
    public void setView(Music music) {
        mToolbar.setTitle(music.getMusicName());
        String path = music.getMusicThumbAlbumCoverPath();
        if (path != null) {
            File coverFile = new File(path);
            Glide.with(this).load(coverFile).into(mCover);
            loadBlurCover(coverFile);
        }
        mTotalTime.setText(TimeUtil.convertLongToMinsSec(music.getMusicDuration()));
        setupSeekBar(music);
    }

    private void initView() {
        if (mCurrentPlayingMusic != null) {
            String musicAlbumUri = mCurrentPlayingMusic.getMusicThumbAlbumCoverPath();
            mToolbar.setTitle(mCurrentPlayingMusic.getMusicName());
            Glide.with(this).load(new File(musicAlbumUri != null ? musicAlbumUri : ""))
                    .into(mCover);
            mTotalTime.setText(TimeUtil.convertLongToMinsSec(mCurrentPlayingMusic.getMusicDuration()));
        }
        setButtonSrc(MediaPlayController.getInstance().isPlaying());
    }

    private void initData() {
        mCurrentPlayingMusic = MusicCache.getInstance().getCurrentPlayingMusic();
        setView(mCurrentPlayingMusic);
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, FinalValue.AnimationProperty.ALPHA, 1f, 0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_pause_white_48dp).into(mPlayOrPauseButton);
            animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, FinalValue.AnimationProperty.ALPHA, 0f, 1f);
            animator.setDuration(382);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, FinalValue.AnimationProperty.ALPHA, 1f, 0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_play_arrow_white_48dp).into(mPlayOrPauseButton);
            animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, FinalValue.AnimationProperty.ALPHA, 0f, 1f);
            animator.setDuration(382);
            animator.start();
        }
    }

    private void setListener() {
        mCover.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayOrPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().registerMusicPlayStateObserver(this);
        MediaPlayController.getInstance().registerProgressChange(this);
        MediaPlayController.getInstance().registerPlayedMusicChangeObserver(this);
    }

    private void unregisterEvent() {
        EventBus.getDefault().unregister(this);
        MediaPlayController.getInstance().unregisterMusicPlayStateObserver(this);
        MediaPlayController.getInstance().unregisterProgressChange(this);
        MediaPlayController.getInstance().unregisterPlayedMusicChangeObserver(this);
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

}
