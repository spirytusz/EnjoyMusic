package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MusicPlayFragmentViewModels;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.impl.binder.PlayMusicChangeObserver;
import com.zspirytus.enjoymusic.impl.binder.PlayProgressChangeObserver;
import com.zspirytus.enjoymusic.impl.binder.PlayStateChangeObserver;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.utils.TimeUtil;
import com.zspirytus.enjoymusic.view.widget.AutoRotateCircleImage;
import com.zspirytus.enjoymusic.view.widget.BlurImageView;
import com.zspirytus.enjoymusic.view.widget.LyricView;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_play_layout)
public class MusicPlayFragment extends BaseFragment
        implements View.OnClickListener, MusicPlayStateObserver,
        MusicPlayProgressObserver, PlayedMusicChangeObserver {

    private static final String MUSIC_KEY = "saveMusic";
    private static final String PLAY_STATE_KEY = "isPlaying";

    @ViewInject(R.id.tool_bar)
    private Toolbar mToolbar;
    @ViewInject(R.id.background)
    private BlurImageView mBackground;
    @ViewInject(R.id.back_btn)
    private AppCompatImageView mBackBtn;
    @ViewInject(R.id.title)
    private AppCompatTextView mTitle;
    @ViewInject(R.id.sub_title)
    private AppCompatTextView mSubTitle;

    @ViewInject(R.id.cover)
    private AutoRotateCircleImage mCover;
    @ViewInject(R.id.lyricView)
    private LyricView mLyricView;

    @ViewInject(R.id.now_time)
    private TextView mNowTime;
    @ViewInject(R.id.music_seekbar)
    private SeekBar mSeekBar;
    @ViewInject(R.id.total_time)
    private TextView mTotalTime;

    @ViewInject(R.id.play_mode)
    private ImageView mPlayMode;
    @ViewInject(R.id.previous)
    private ImageView mPreviousButton;
    @ViewInject(R.id.play_pause)
    private ImageView mPlayOrPauseButton;
    @ViewInject(R.id.next)
    private ImageView mNextButton;

    private MusicPlayFragmentViewModels mViewModel;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
                ForegroundMusicController.getInstance().playPrevious();
                break;
            case R.id.play_pause:
                boolean isPlaying = ForegroundMusicController.getInstance().isPlaying();
                Music currentPlayingMusic = mViewModel.getCurrentPlayingMusic().getValue();
                if (isPlaying) {
                    pause();
                } else {
                    continueToPlay(currentPlayingMusic);
                }
                break;
            case R.id.next:
                ForegroundMusicController.getInstance().playNext(true);
                break;
            case R.id.play_mode:
                int mode = 1;
                mode %= mViewModel.getPlayModeResId().size();
                mPlayMode.setImageResource(mViewModel.getPlayModeResId().get(mode));
                ForegroundMusicController.getInstance().setPlayMode(mode);
                break;
            case R.id.cover:
            case R.id.lyricView:
                if (mCover.getAlpha() == 1.0f && mLyricView.getAlpha() == 0.0f) {
                    mCover.animate().alpha(0).setDuration(618).setInterpolator(new DecelerateInterpolator()).start();
                    mLyricView.animate().alpha(1).setDuration(618).setInterpolator(new DecelerateInterpolator()).start();
                } else if (mCover.getAlpha() == 0.0f && mLyricView.getAlpha() == 1.0f) {
                    mCover.animate().alpha(1).setDuration(618).setInterpolator(new DecelerateInterpolator()).start();
                    mLyricView.animate().alpha(0).setDuration(618).setInterpolator(new DecelerateInterpolator()).start();
                }
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getParentActivity().setTransparentNavBar();
            getParentActivity().setDefaultStatusIconColor();
        } else {
            getParentActivity().setDefaultNavBar();
            getParentActivity().setLightStatusIconColor();
        }
    }

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(MusicPlayFragmentViewModels.class);
        mViewModel.init();
    }

    @Override
    protected void initView() {
        getParentActivity().setDefaultStatusIconColor();
        getParentActivity().setTransparentNavBar();
        mBackBtn.setOnClickListener((view) -> goBack());
        setButtonSrc(ForegroundMusicController.getInstance().isPlaying());
        mCover.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayOrPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPlayMode.setOnClickListener(this);
        mPlayMode.setImageResource(mViewModel.getPlayModeResId().get(0));
        mToolbar.inflateMenu(R.menu.music_play_fragment_menu);
        mToolbar.setOnMenuItemClickListener((item -> {
            switch (item.getItemId()) {
                case R.id.menu_edit_music_info:
                    showMusicMetaDataFragment();
                    return true;
            }
            return false;
        }));
        mCover.setOnClickListener(this);
        mLyricView.setOnClickListener(this);
        mLyricView.setAlpha(0.0f);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Music currentMusic = mViewModel.getCurrentPlayingMusic().getValue();
        Boolean isPlaying = mViewModel.getPlayState().getValue();
        if (currentMusic != null) {
            outState.putParcelable(MUSIC_KEY, currentMusic);
        }
        if (isPlaying != null) {
            outState.putBoolean(PLAY_STATE_KEY, isPlaying);
        } else {
            outState.putBoolean(PLAY_STATE_KEY, false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getPlayProgress().observe(this, (values) -> {
            if (values != null) {
                mSeekBar.setProgress(values / 1000);
                mLyricView.onPlayProgressChange(values);
            }
        });
        mViewModel.getPlayState().observe(this, (values) -> {
            if (values != null) {
                setButtonSrc(values);
                mCover.setRotating(values);
            }
        });
        mViewModel.getCurrentPlayingMusic().observe(this, (values) -> {
            if (values != null) {
                setView(values);
                mCover.resetRotation();
            }
        });
        if (savedInstanceState != null) {
            Music saveMusic = savedInstanceState.getParcelable(MUSIC_KEY);
            Boolean isPlaying = savedInstanceState.getBoolean(PLAY_STATE_KEY);
            if (saveMusic != null) {
                setView(saveMusic);
            }
            setButtonSrc(isPlaying);
            mCover.setRotating(isPlaying);
        }
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onProgressChanged(int progress) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> mViewModel.setPlayProgress(progress));
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> mViewModel.setPlayState(isPlaying));
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> mViewModel.setCurrentPlayingMusic(music));
    }

    @Override
    protected void registerEvent() {
        PlayMusicChangeObserver.getInstance().register(this);
        PlayProgressChangeObserver.getInstance().register(this);
        PlayStateChangeObserver.getInstance().register(this);
    }

    @Override
    protected void unregisterEvent() {
        PlayMusicChangeObserver.getInstance().unregister(this);
        PlayProgressChangeObserver.getInstance().unregister(this);
        PlayStateChangeObserver.getInstance().unregister(this);
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().hide(this);
        getParentActivity().setDefaultNavBar();
    }

    private void continueToPlay(Music currentPlayingMusic) {
        ForegroundMusicController.getInstance().play(currentPlayingMusic);
    }

    private void pause() {
        if (mViewModel.getCurrentPlayingMusic().getValue() != null) {
            ForegroundMusicController.getInstance().pause();
        }
    }

    private void setButtonSrc(boolean isPlaying) {
        int resId = isPlaying ? R.drawable.ic_pause_pressed : R.drawable.ic_play_pressed;
        GlideApp.with(this).load(resId).into(mPlayOrPauseButton);
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
            if (coverFile.exists()) {
                GlideApp.with(this)
                        .load(coverFile)
                        .into(mCover);
            } else {
                GlideApp.with(this)
                        .load(R.drawable.defalut_cover)
                        .into(mCover);
            }
        } else {
            GlideApp.with(this)
                    .load(R.drawable.defalut_cover)
                    .into(mCover);
        }
        mTitle.setText(music.getMusicName());
        mSubTitle.setText(music.getMusicArtist());
        mNowTime.setText("00:00");
        mTotalTime.setText(TimeUtil.convertLongToMinsSec(music.getMusicDuration()));
        setupSeekBar(music);
        setBackgroundBlur(music);
    }

    private void setBackgroundBlur(Music music) {
        String imagePath = music.getMusicThumbAlbumCoverPath();
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                mBackground.setImagePath(imagePath);
            } else {
                mBackground.setImageResource(R.drawable.defalut_cover);
            }
        } else {
            mBackground.setImageResource(R.drawable.defalut_cover);
        }
    }

    private void showMusicMetaDataFragment() {
        MusicMetaDataFragment fragment = MusicMetaDataFragment.getInstance(mViewModel.getCurrentPlayingMusic().getValue());
        FragmentVisibilityManager.getInstance().addToBackStack(this);
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    public static MusicPlayFragment getInstance() {
        return new MusicPlayFragment();
    }

}
