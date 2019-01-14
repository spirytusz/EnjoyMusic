package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.impl.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.impl.binder.IPlayProgressChangeObserverImpl;
import com.zspirytus.enjoymusic.impl.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.utils.TimeUtil;
import com.zspirytus.enjoymusic.view.widget.AutoRotateCircleImage;
import com.zspirytus.enjoymusic.view.widget.BlurImageView;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_play_layout)
public class MusicPlayFragment extends BaseFragment implements View.OnClickListener, MusicPlayStateObserver,
        MusicPlayProgressObserver, PlayedMusicChangeObserver {

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

    private Music mCurrentPlayingMusic;
    private SparseIntArray mPlayModeResId;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
                ForegroundMusicController.getInstance().playPrevious();
                break;
            case R.id.play_pause:
                boolean isPlaying = ForegroundMusicController.getInstance().isPlaying();
                Music currentPlayingMusic = ForegroundMusicStateCache.getInstance().getCurrentPlayingMusic();
                if (isPlaying) {
                    ForegroundMusicController.getInstance().pause();
                } else {
                    ForegroundMusicController.getInstance().play(currentPlayingMusic);
                }
                break;
            case R.id.next:
                ForegroundMusicController.getInstance().playNext(true);
                break;
            case R.id.play_mode:
                int mode = ForegroundMusicStateCache.getInstance().getPlayMode() + 1;
                mode %= mPlayModeResId.size();
                mPlayMode.setImageResource(mPlayModeResId.get(mode));
                ForegroundMusicController.getInstance().setPlayMode(mode);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mCover.setRotating(!hidden);
        if (!hidden) {
            getParentActivity().setTransparentNavBar();
        } else {
            getParentActivity().setDefaultNavBar();
        }
    }

    @Override
    public void onProgressChanged(final int progress) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            mSeekBar.setProgress(progress);
        });
    }

    @Override
    public void onPlayingStateChanged(final boolean isPlaying) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            setButtonSrc(isPlaying);
            mCover.setRotating(isPlaying);
        });
    }

    @Override
    public void onPlayedMusicChanged(final Music music) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            mCurrentPlayingMusic = music;
            setView(music);
            mCover.resetRotation();
        });
    }

    @Override
    protected void initData() {
        mCurrentPlayingMusic = getArguments().getParcelable("music");
        if (mCurrentPlayingMusic == null) {
            mCurrentPlayingMusic = ForegroundMusicStateCache.getInstance().getCurrentPlayingMusic();
        }
        mPlayModeResId = new SparseIntArray();
        mPlayModeResId.put(Constant.PlayMode.LIST_LOOP, R.drawable.ic_list_loop_pressed);
        mPlayModeResId.put(Constant.PlayMode.RANDOM, R.drawable.ic_random_pressed);
        mPlayModeResId.put(Constant.PlayMode.SINGLE_LOOP, R.drawable.ic_single_loop_pressed);
    }

    @Override
    protected void initView() {
        getParentActivity().setTransparentNavBar();
        mBackBtn.setOnClickListener((view) -> {
            goBack();
        });
        if (mCurrentPlayingMusic != null) {
            setView(mCurrentPlayingMusic);
        }
        setButtonSrc(ForegroundMusicController.getInstance().isPlaying());
        mCover.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayOrPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPlayMode.setOnClickListener(this);
        mPlayMode.setImageResource(mPlayModeResId.get(0));
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
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

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().hide(this);
        getParentActivity().setDefaultNavBar();
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

    public static MusicPlayFragment getInstance() {
        return new MusicPlayFragment();
    }

}
