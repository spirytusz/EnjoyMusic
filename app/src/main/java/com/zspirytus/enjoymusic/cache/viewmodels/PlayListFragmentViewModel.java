package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.impl.binder.PlayListObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlayListFragmentViewModel extends ViewModel
        implements PlayListChangeDirectlyObserver {

    private MutableLiveData<List<Music>> mPlayList;
    private List<Music> mAllMusicList;

    public void init(List<Music> musicList) {
        mPlayList = new MutableLiveData<>();
        mAllMusicList = musicList;
        PlayListObserverManager.getInstance().register(this);
    }

    public MutableLiveData<List<Music>> getPlayList() {
        return mPlayList;
    }

    @Override
    public void onPlayListChangeDirectly(List<Music> playList) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> mPlayList.setValue(playList));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        PlayListObserverManager.getInstance().unregister(this);
    }
}
