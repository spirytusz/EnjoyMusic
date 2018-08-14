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
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.services.MediaPlayController;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

public class MusicPlayFragment extends BaseFragment
        implements View.OnClickListener, MusicPlayStateObserver {

    private static final String MUSIC_KEY = "music_key";
    private static MediaPlayController mediaPlayController = MediaPlayController.getInstance();

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
    public Integer getLayoutId() {
        return R.layout.fragment_music_play;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.cover:
                break;
            case R.id.previous:
                break;
            case R.id.play_pause:
                boolean isPlaying = MediaPlayController.isPlaying();
                MusicCache musicCache = MusicCache.getInstance();
                if (isPlaying) {
                    EventBus.getDefault().post(musicCache.getCurrentPlayingMusic(), "pause");
                } else {
                    EventBus.getDefault().post(musicCache.getCurrentPlayingMusic(), "play");
                }
                break;
            case R.id.next:
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
        Music music = (Music) getArguments().getSerializable(MUSIC_KEY);
        if (music != null) {
            mToolbar.setTitle(music.getmMusicName());
            Glide.with(this).load(new File(music.getmMusicThumbAlbumUri()))
                    .into(mCover);
            mTotalTime.setText(music.getDuration());
        }
        setButtonSrc(MediaPlayController.isPlaying());
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayOrPauseButton,"alpha",1f,0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_pause_thin).into(mPlayOrPauseButton);
            animator = ObjectAnimator.ofFloat(mPlayOrPauseButton,"alpha",0f,1f);
            animator.setDuration(382);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayOrPauseButton,"alpha",1f,0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_play_thin).into(mPlayOrPauseButton);
            animator = ObjectAnimator.ofFloat(mPlayOrPauseButton,"alpha",0f,1f);
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
    }

    private void unRegisterEvent() {
        EventBus.getDefault().unregister(this);
        MediaPlayController.getInstance().unregister(this);
    }

    @Subscriber(tag = "music_name_set")
    public void setView(Music music) {
        mToolbar.setTitle(music.getmMusicName());
        Glide.with(this).load(new File(music.getmMusicThumbAlbumUri())).into(mCover);
        mTotalTime.setText(music.getDuration());
    }

}
