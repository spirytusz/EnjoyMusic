package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.table.Music;

import java.util.Collections;
import java.util.List;

public class HomePageFragmentViewModel extends ViewModel {

    private static final int LIMIT_SIZE = 50;
    private MutableLiveData<List<Music>> mMusicList = new MutableLiveData<>();

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicList;
    }

    public void applyMusicList(List<Music> musicList) {
        ThreadPool.execute(() -> {
            Collections.sort(musicList, (o1, o2) -> (int) (o1.getMusicAddDate() - o2.getMusicAddDate()));
            mMusicList.postValue(musicList.subList(0, musicList.size() > LIMIT_SIZE ? LIMIT_SIZE : musicList.size()));
        });
    }
}
