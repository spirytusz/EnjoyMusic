package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayListObserverManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayMusicObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.List;

public class PlayListFragmentViewModel extends ViewModel
        implements PlayListChangeObserver, PlayedMusicChangeObserver {

    private MutableLiveData<List<Music>> mPlayList;
    private MutableLiveData<Music> mCurrentPlayMusic;

    public void init() {
        mPlayList = new MutableLiveData<>();
        mCurrentPlayMusic = new MutableLiveData<>();
        PlayListObserverManager.getInstance().register(this);
        PlayMusicObserverManager.getInstance().register(this);
    }

    public MutableLiveData<List<Music>> getPlayList() {
        return mPlayList;
    }

    public MutableLiveData<Music> getCurrentPlayMusic() {
        return mCurrentPlayMusic;
    }

    @Override
    public void onPlayListChangeDirectly(List<Music> playList) {
        mPlayList.postValue(playList);
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        mCurrentPlayMusic.postValue(music);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        PlayListObserverManager.getInstance().unregister(this);
        PlayMusicObserverManager.getInstance().unregister(this);
    }
}
