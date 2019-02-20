package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.impl.binder.PlayListObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlayListFragmentViewModel extends ViewModel
        implements PlayListChangeObserver, PlayListChangeDirectlyObserver {

    private MutableLiveData<List<Music>> mPlayList;
    private List<Music> mAllMusicList;

    public void init(List<Music> musicList) {
        mPlayList = new MutableLiveData<>();
        PlayListObserverManager.getInstance().register((PlayListChangeDirectlyObserver) this);
        PlayListObserverManager.getInstance().register((PlayListChangeObserver) this);
    }

    public MutableLiveData<List<Music>> getPlayList() {
        return mPlayList;
    }

    @Override
    public void onPlayListChangeDirectly(List<Music> playList) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> mPlayList.setValue(playList));
    }

    @Override
    public void onPlayListChanged(MusicFilter filter) {
        List<Music> musicList = filter.filter(mAllMusicList);
        AndroidSchedulers.mainThread().scheduleDirect(() -> mPlayList.setValue(musicList));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        PlayListObserverManager.getInstance().unregister((PlayListChangeDirectlyObserver) this);
        PlayListObserverManager.getInstance().unregister((PlayListChangeObserver) this);
    }
}
