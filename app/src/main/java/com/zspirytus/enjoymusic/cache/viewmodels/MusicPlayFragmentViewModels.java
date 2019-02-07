package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;

public class MusicPlayFragmentViewModels extends ViewModel {

    private MutableLiveData<Boolean> mPlayState = new MutableLiveData<>();
    private MutableLiveData<Integer> mPlayProgress = new MutableLiveData<>();
    private MutableLiveData<Music> mCurrentPlayingMusic = new MutableLiveData<>();

    private SparseIntArray mPlayModeResId;

    public void init() {
        mPlayModeResId = new SparseIntArray();
        mPlayModeResId.put(Constant.PlayMode.LIST_LOOP, R.drawable.ic_list_loop_pressed);
        mPlayModeResId.put(Constant.PlayMode.RANDOM, R.drawable.ic_random_pressed);
        mPlayModeResId.put(Constant.PlayMode.SINGLE_LOOP, R.drawable.ic_single_loop_pressed);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mPlayState = null;
        mPlayProgress = null;
        mCurrentPlayingMusic = null;
        mPlayModeResId = null;
    }

    public MutableLiveData<Boolean> getPlayState() {
        return mPlayState;
    }

    public void setPlayState(boolean isPlaying) {
        mPlayState.setValue(isPlaying);
    }

    public MutableLiveData<Integer> getPlayProgress() {
        return mPlayProgress;
    }

    public void setPlayProgress(int progress) {
        mPlayProgress.setValue(progress);
    }

    public MutableLiveData<Music> getCurrentPlayingMusic() {
        return mCurrentPlayingMusic;
    }

    public void setCurrentPlayingMusic(Music currentPlayingMusic) {
        mCurrentPlayingMusic.setValue(currentPlayingMusic);
    }

    public SparseIntArray getPlayModeResId() {
        return mPlayModeResId;
    }
}
