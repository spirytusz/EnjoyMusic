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

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MusicPlayFragmentViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.TimeUtil;
import com.zspirytus.enjoymusic.view.dialog.PlayHistoryDialog;
import com.zspirytus.enjoymusic.view.widget.AutoRotateCircleImage;
import com.zspirytus.enjoymusic.view.widget.BlurImageView;
import com.zspirytus.enjoymusic.view.widget.LyricView;

import java.io.File;

/**
 * Fragment: 显示音乐播放界面
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_play_layout)
public class MusicPlayFragment extends BaseFragment implements View.OnClickListener {

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
    @ViewInject(R.id.play_history)
    private ImageView mPlayHistoryBtn;

    private MainActivityViewModel mViewModel;
    private MusicPlayFragmentViewModel viewModel;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.previous:
                ForegroundMusicController.getInstance().playPrevious();
                break;
            case R.id.play_pause:
                boolean isPlaying = mViewModel.getPlayState().getValue();
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
                int mode = mViewModel.getPlayMode().getValue() + 1;
                mode %= mViewModel.getPlayModeResId().size();
                mViewModel.setPlayMode(mode);
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
        mViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
        mViewModel.init();
        mViewModel.obtainPlayMode(getContext());
        viewModel = ViewModelProviders.of(this).get(MusicPlayFragmentViewModel.class);
    }

    @Override
    protected void initView() {
        getParentActivity().setDefaultStatusIconColor();
        getParentActivity().setTransparentNavBar();
        mBackBtn.setOnClickListener((view) -> goBack());
        setButtonSrc(false);
        mCover.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);
        mPlayOrPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPlayMode.setOnClickListener(this);
        mToolbar.inflateMenu(R.menu.music_play_fragment_menu);
        mToolbar.setOnMenuItemClickListener((item -> {
            switch (item.getItemId()) {
                case R.id.menu_edit_music_info:
                    showMusicMetaDataFragment();
                    return true;
                case R.id.menu_audio_field:
                    showAudioEffectFragment(AudioEffectFragment.FLAG_AUDIO_FILED);
                    break;
                case R.id.menu_equalizer:
                    showAudioEffectFragment(AudioEffectFragment.FLAG_EQUALIZER);
                    break;
                /*case R.id.menu_bass_boast:
                    showAudioEffectFragment(AudioEffectFragment.FLAG_BASS_BOAST);
                    break;*/
                case R.id.menu_lyric_download:
                    viewModel.applyLyricFromNetWork(mViewModel.getCurrentPlayingMusic().getValue());
                    break;
            }
            return false;
        }));
        mCover.setOnClickListener(this);
        mLyricView.setOnClickListener(this);
        mLyricView.setAlpha(0);
        mPlayHistoryBtn.setOnClickListener(v -> FragmentVisibilityManager.getInstance().showDialogFragment(new PlayHistoryDialog()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getPlayProgress().observe(getParentActivity(), (values) -> {
            if (values != null) {
                mSeekBar.setProgress(values / 1000);
                mLyricView.onPlayProgressChange(values);
            }
        });
        mViewModel.getPlayState().observe(getParentActivity(), (values) -> {
            if (values != null) {
                setButtonSrc(values);
                mCover.setRotating(values);
            }
        });
        mViewModel.getCurrentPlayingMusic().observe(getParentActivity(), values -> {
            setView(values);
            viewModel.applyLyricFromDB(values);
        });
        mViewModel.getPlayMode().observe(getParentActivity(), (values) -> {
            mPlayMode.setImageResource(mViewModel.getPlayModeResId().get(values));
            ForegroundMusicController.getInstance().setPlayMode(values);
        });
        viewModel.getLyricRows().observe(this, values -> mLyricView.setLyricRows(values));
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
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
        Album album = QueryExecutor.findAlbum(music);
        Artist artist = QueryExecutor.findArtist(music);
        String musicThumbAlbumCoverPath = album.getAlbumArt();
        ImageLoader.load(mCover, musicThumbAlbumCoverPath, music.getMusicName(), new CenterCrop());
        mTitle.setText(music.getMusicName());
        mSubTitle.setText(artist.getArtistName());
        mNowTime.setText("00:00");
        mTotalTime.setText(TimeUtil.convertLongToMinsSec(music.getMusicDuration()));
        setupSeekBar(music);
        setBackgroundBlur(music);
    }

    private void setBackgroundBlur(Music music) {
        Album album = QueryExecutor.findAlbum(music);
        String imagePath = album.getAlbumArt();
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

    private void showAudioEffectFragment(int flag) {
        AudioEffectFragment fragment = AudioEffectFragment.getInstance(flag);
        FragmentVisibilityManager.getInstance().addToBackStack(this);
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    public static MusicPlayFragment getInstance() {
        return new MusicPlayFragment();
    }

}
