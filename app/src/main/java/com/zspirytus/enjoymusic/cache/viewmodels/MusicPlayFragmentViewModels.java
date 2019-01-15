package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.impl.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.impl.binder.IPlayProgressChangeObserverImpl;
import com.zspirytus.enjoymusic.impl.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MusicPlayFragmentViewModels extends ViewModel
        implements MusicPlayStateObserver, MusicPlayProgressObserver,
        PlayedMusicChangeObserver {

    private MutableLiveData<Boolean> mPlayState = new MutableLiveData<>();
    private MutableLiveData<Integer> mPlayProgress = new MutableLiveData<>();
    private MutableLiveData<Music> mCurrentPlayingMusic = new MutableLiveData<>();

    @Override
    public void onProgressChanged(int progress) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> setPlayProgress(progress));
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> setPlayState(isPlaying));
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> setCurrentPlayingMusic(music));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        IPlayStateChangeObserverImpl.getInstance().unregister(this);
        IPlayMusicChangeObserverImpl.getInstance().unregister(this);
        IPlayProgressChangeObserverImpl.getInstance().unregister(this);
    }

    public void init() {
        IPlayStateChangeObserverImpl.getInstance().register(this);
        IPlayMusicChangeObserverImpl.getInstance().register(this);
        IPlayProgressChangeObserverImpl.getInstance().register(this);
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
}
