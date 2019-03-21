package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;

import java.util.List;

public class AllMusicListFragmentViewModel extends ViewModel {

    private MutableLiveData<Music> mPlayListFirstMusic;

    public void init() {
        mPlayListFirstMusic = new MutableLiveData<>();
    }

    public MutableLiveData<Music> getPlayListFirstMusic() {
        return mPlayListFirstMusic;
    }

    public void setPlayList(List<Music> musicList) {
        if (musicList != null) {
            ThreadPool.execute(() -> {
                Music firstMusic = ForegroundMusicController.getInstance().setPlayListAndGetFirstMusic(musicList);
                mPlayListFirstMusic.postValue(firstMusic);
            });
        }
    }
}
