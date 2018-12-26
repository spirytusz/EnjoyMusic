package com.zspirytus.enjoymusic.view.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayProgressChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.IFragmentBackable;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnMultiEventImageViewListener;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.utils.BitmapUtil;
import com.zspirytus.enjoymusic.utils.ColorUtils;
import com.zspirytus.enjoymusic.utils.DrawableUtil;
import com.zspirytus.enjoymusic.utils.TimeUtil;
import com.zspirytus.enjoymusic.view.widget.MultiEventImageView;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_play_layout)
public class MusicPlayFragment extends CommonHeaderBaseFragment implements View.OnClickListener, MusicPlayStateObserver,
        MusicPlayProgressObserver, PlayedMusicChangeObserver, IFragmentBackable {

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
    private int selfAdaptionColor = Color.parseColor("#FFFFFF");

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
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            mSeekBar.setProgress(progress);
        });
    }

    @Override
    public void onPlayingStateChanged(final boolean isPlaying) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            setButtonSrc(isPlaying);
        });
    }

    @Override
    public void onPlayedMusicChanged(final Music music) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            mCurrentPlayingMusic = music;
            setView(music);
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
        mNavIcon.setOnClickListener(v -> {
            goBack();
        });
        setHeaderViewColor(Color.TRANSPARENT);
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

    @Override
    public void goBack() {
        getFragmentManager().beginTransaction()
                .hide(this)
                .commitAllowingStateLoss();
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            GlideApp.with(this).load(DrawableUtil.setColor(getContext(), R.drawable.ic_pause_black_48dp, selfAdaptionColor)).into(mPlayOrPauseButton);
        } else {
            GlideApp.with(this).load(DrawableUtil.setColor(getContext(), R.drawable.ic_play_arrow_black_48dp, selfAdaptionColor)).into(mPlayOrPauseButton);
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
        if (file != null && file.exists()) {
            Bitmap source = BitmapFactory.decodeFile(file.getAbsolutePath());
            Bitmap bitmapBlur = BitmapUtil.bitmapBlur(getContext(), source, 25);
            GlideApp.with(getParentActivity())
                    .load(bitmapBlur)
                    .into(mBackground);
            ColorUtils.setSelfAdaptionColor(bitmapBlur, (palette -> {
                Palette.Swatch swatch = palette.getVibrantSwatch();
                int rgb;
                if (swatch != null && (rgb = swatch.getTitleTextColor()) != 0) {
                    selfAdaptionColor = rgb;
                    setPanelColor();
                } else {
                    selfAdaptionColor = Color.parseColor("#FFFFFF");
                    setPanelColor();
                }
            }));
        }
    }

    private void setPanelColor() {
        setTitleColor(selfAdaptionColor);
        setNavIconColor(selfAdaptionColor);
        mPlayOrPauseButton.setImageDrawable(DrawableUtil.setColor(mPlayOrPauseButton.getDrawable(), selfAdaptionColor));
        mPreviousButton.setImageDrawable(DrawableUtil.setColor(mPreviousButton.getDrawable(), selfAdaptionColor));
        mNextButton.setImageDrawable(DrawableUtil.setColor(mNextButton.getDrawable(), selfAdaptionColor));
    }

    public static MusicPlayFragment getInstance() {
        return new MusicPlayFragment();
    }

}
