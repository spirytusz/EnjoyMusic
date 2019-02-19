package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.entity.table.SongListItem;

import org.litepal.LitePal;

import java.util.List;

// TODO: 19/02/2019 implements get song list
public class SongListFragmentViewModel extends ViewModel {

    private MutableLiveData<List<SongListItem>> mSongLists;

    public void init() {
        mSongLists = new MutableLiveData<>();
    }

    public MutableLiveData<List<SongListItem>> getSongList() {
        return mSongLists;
    }

    public void applySongList() {
        List<SongListItem> songListItems = LitePal.findAll(SongListItem.class);
        mSongLists.setValue(songListItems);
    }

    public void applySongListMusic() {
    }
}
