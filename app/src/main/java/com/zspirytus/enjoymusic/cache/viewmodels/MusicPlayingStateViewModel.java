package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayMusicObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayStateObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.ProgressObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

public class MusicPlayingStateViewModel extends MusicDataViewModel implements PlayedMusicChangeObserver,
        MusicPlayStateObserver, MusicPlayProgressObserver {

    private static final String MUSIC_KEY = "saveMusic";
    private static final String PLAY_STATE_KEY = "isPlaying";

    private MutableLiveData<Boolean> mPlayState = new MutableLiveData<>();
    private MutableLiveData<Integer> mPlayProgress = new MutableLiveData<>();
    private MutableLiveData<Music> mCurrentPlayingMusic = new MutableLiveData<>();
    private MutableLiveData<Integer> mPlayMode = new MutableLiveData<>();

    private SparseIntArray mPlayModeResId;

    public void init() {
        super.init();
        mPlayModeResId = new SparseIntArray();
        mPlayModeResId.put(Constant.PlayMode.LIST_LOOP, R.drawable.ic_list_loop_pressed);
        mPlayModeResId.put(Constant.PlayMode.RANDOM, R.drawable.ic_random_pressed);
        mPlayModeResId.put(Constant.PlayMode.SINGLE_LOOP, R.drawable.ic_single_loop_pressed);

        PlayMusicObserverManager.getInstance().register(this);
        PlayStateObserverManager.getInstance().register(this);
        ProgressObserverManager.getInstance().register(this);
    }

    @Override
    public void onPlayedMusicChanged(final Music music) {
        if (music != null) {
            postCurrentPlayingMusic(music);
        }
    }

    @Override
    public void onPlayingStateChanged(final boolean isPlaying) {
        postPlayState(isPlaying);
    }

    @Override
    public void onProgressChanged(int progress) {
        postPlayProgress(progress);
    }

    public void onSaveInstanceState(Bundle outState) {
        Music currentMusic = getCurrentPlayingMusic().getValue();
        Boolean isPlaying = getPlayState().getValue();
        if (currentMusic != null) {
            outState.putParcelable(MUSIC_KEY, currentMusic);
        }
        if (isPlaying != null) {
            outState.putBoolean(PLAY_STATE_KEY, isPlaying);
        } else {
            outState.putBoolean(PLAY_STATE_KEY, false);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Music saveMusic = savedInstanceState.getParcelable(MUSIC_KEY);
            boolean isPlaying = savedInstanceState.getBoolean(PLAY_STATE_KEY);
            if (saveMusic != null) {
                setCurrentPlayingMusic(saveMusic);
            }
            setPlayState(isPlaying);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mPlayState = null;
        mPlayProgress = null;
        mCurrentPlayingMusic = null;
        mPlayModeResId = null;

        PlayMusicObserverManager.getInstance().unregister(this);
        PlayStateObserverManager.getInstance().unregister(this);
        ProgressObserverManager.getInstance().unregister(this);
    }

    public MutableLiveData<Boolean> getPlayState() {
        return mPlayState;
    }

    public void setPlayState(boolean isPlaying) {
        mPlayState.setValue(isPlaying);
    }

    public void postPlayState(boolean isPlaying) {
        mPlayState.postValue(isPlaying);
    }

    public MutableLiveData<Integer> getPlayProgress() {
        return mPlayProgress;
    }

    public void setPlayProgress(int progress) {
        mPlayProgress.setValue(progress);
    }

    public void postPlayProgress(int progress) {
        mPlayProgress.postValue(progress);
    }

    public MutableLiveData<Music> getCurrentPlayingMusic() {
        return mCurrentPlayingMusic;
    }

    public void setCurrentPlayingMusic(Music currentPlayingMusic) {
        mCurrentPlayingMusic.setValue(currentPlayingMusic);
    }

    public void postCurrentPlayingMusic(Music currentPlayingMusic) {
        mCurrentPlayingMusic.postValue(currentPlayingMusic);
    }

    public MutableLiveData<Integer> getPlayMode() {
        return mPlayMode;
    }

    public void setPlayMode(int playMode) {
        mPlayMode.setValue(playMode);
    }

    public void postPlayMode(int playMode) {
        mPlayMode.postValue(playMode);
    }

    public SparseIntArray getPlayModeResId() {
        return mPlayModeResId;
    }

    public void obtainPlayMode(Context context) {
        setPlayMode(MusicSharedPreferences.restorePlayMode(context));
    }
}
