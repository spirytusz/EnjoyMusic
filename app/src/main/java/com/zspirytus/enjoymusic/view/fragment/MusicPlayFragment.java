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
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.receivers.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.utils.LogUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

public class MusicPlayFragment extends BaseFragment
        implements View.OnClickListener, MusicPlayStateObserver,
        MusicPlayProgressObserver {

    private static final String MUSIC_KEY = "music_key";
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

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

    public static MusicPlayFragment getInstance(Music music) {
        MusicPlayFragment musicPlayFragment = new MusicPlayFragment();
        Bundle bundle = new Bundle(1);
        bundle.putSerializable(MUSIC_KEY, music);
        musicPlayFragment.setArguments(bundle);
        return musicPlayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        registerEvent();
        initView();
        setListener();
        return view;
    }

    @Override
    public void onDestroyView() {
        unRegisterEvent();
        super.onDestroyView();
    }

    @Override
    public void onProgressChange(int progress) {
        mSeekBar.setProgress(progress);
    }

    @Override
    public Integer getLayoutId() {
        return R.layout.fragment_music_play;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
                Music previousMusic = MusicCache.getInstance().getPreviousMusic(MusicCache.MODE_ORDER);
                EventBus.getDefault().post(previousMusic, "play");
                setView(previousMusic);
                break;
            case R.id.play_pause:
                boolean isPlaying = MediaPlayController.getInstance().isPlaying();
                MusicCache musicCache = MusicCache.getInstance();
                if (isPlaying) {
                    EventBus.getDefault().post(musicCache.getCurrentPlayingMusic(), "pause");
                } else {
                    EventBus.getDefault().post(musicCache.getCurrentPlayingMusic(), "play");
                }
                break;
            case R.id.next:
                Music nextMusic = MusicCache.getInstance().getNextMusic(MusicCache.MODE_ORDER);
                EventBus.getDefault().post(nextMusic, "play");
                setView(nextMusic);
                break;
        }
    }

    @Override
    public void onPlayingState(boolean isPlaying) {
        setButtonSrc(isPlaying);
    }

    @Override
    public void onPlayCompleted() {
        // TODO: 2018/8/13 music loop or next or random play 
    }

    private void initView() {
        final Music music = (Music) getArguments().getSerializable(MUSIC_KEY);
        if (music != null) {
            String musicAlbumUri = music.getmMusicThumbAlbumUri();
            mToolbar.setTitle(music.getmMusicName());
            Glide.with(this).load(new File(musicAlbumUri != null ? musicAlbumUri : ""))
                    .into(mCover);
            mTotalTime.setText(simpleDateFormat.format(new Date(music.getDuration())));
        }
        setButtonSrc(MediaPlayController.getInstance().isPlaying());
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                EventBus.getDefault().post(progress, "seek to");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                float progress = seekBar.getProgress() / 100;
                long seekPosition = (long) (music.getDuration() * progress);
                LogUtil.e(this.getClass().getSimpleName(), seekPosition + "");
                mNowTime.setText(simpleDateFormat.format(new Date(seekPosition)));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, "alpha", 1f, 0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_pause_white_48dp).into(mPlayOrPauseButton);
            animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, "alpha", 0f, 1f);
            animator.setDuration(382);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, "alpha", 1f, 0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_play_arrow_white_48dp).into(mPlayOrPauseButton);
            animator = ObjectAnimator.ofFloat(mPlayOrPauseButton, "alpha", 0f, 1f);
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
        MediaPlayController.getInstance().register(this);
        MediaPlayController.getInstance().registerProgressChange(this);
    }

    private void unRegisterEvent() {
        EventBus.getDefault().unregister(this);
        MediaPlayController.getInstance().unregister(this);
        MediaPlayController.getInstance().unregisterProgressChange(this);
    }

    @Subscriber(tag = "music_name_set")
    public void setView(Music music) {
        mToolbar.setTitle(music.getmMusicName());
        Glide.with(this).load(new File(music.getmMusicThumbAlbumUri())).into(mCover);
        mTotalTime.setText(simpleDateFormat.format(music.getDuration()));
    }

}
