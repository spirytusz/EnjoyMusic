package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;

import java.util.List;

public class HomePageFragmentViewModel extends ViewModel {

    private static final int LIMIT_SIZE = 50;
    private MutableLiveData<List<Music>> mMusicList;
    private MutableLiveData<Music> mPlayListFirstMusic;

    public void init() {
        mMusicList = new MutableLiveData<>();
        mPlayListFirstMusic = new MutableLiveData<>();
    }

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicList;
    }

    public MutableLiveData<Music> getPlayListFirstMusic() {
        return mPlayListFirstMusic;
    }

    public void applyMusicList() {
        ThreadPool.execute(() -> {
            List<Music> sortedMusicList = DBManager.getInstance().getDaoSession().queryBuilder(Music.class)
                    .orderDesc(MusicDao.Properties.MusicAddDate)
                    .limit(LIMIT_SIZE)
                    .list();
            mMusicList.postValue(sortedMusicList);
        });
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
