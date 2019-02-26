package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.PlayListObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver;

import java.util.List;

public class PlayListFragmentViewModel extends ViewModel
        implements PlayListChangeDirectlyObserver {

    private MutableLiveData<List<Music>> mPlayList;

    public void init() {
        mPlayList = new MutableLiveData<>();
        PlayListObserverManager.getInstance().register(this);
    }

    public MutableLiveData<List<Music>> getPlayList() {
        return mPlayList;
    }

    @Override
    public void onPlayListChangeDirectly(List<Music> playList) {
        mPlayList.postValue(playList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        PlayListObserverManager.getInstance().unregister(this);
    }
}
