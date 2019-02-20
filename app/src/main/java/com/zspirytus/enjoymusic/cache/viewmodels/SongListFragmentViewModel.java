package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.SongList;

import java.util.List;

// TODO: 19/02/2019 implements get song list
public class SongListFragmentViewModel extends ViewModel {

    private MutableLiveData<List<SongList>> mSongLists;

    public void init() {
        mSongLists = new MutableLiveData<>();
    }

    public MutableLiveData<List<SongList>> getSongList() {
        return mSongLists;
    }

    public void applySongList() {
        List<SongList> songLists = DBManager.getInstance().getDaoSession().loadAll(SongList.class);
        mSongLists.setValue(songLists);
    }
}
