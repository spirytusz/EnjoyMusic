package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.Music;

import java.util.List;

public class HomePageFragmentViewModel extends ViewModel {

    private static final int LIMIT_SIZE = 30;
    private MutableLiveData<List<Music>> mMusicList = new MutableLiveData<>();

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicList;
    }

    public void applyMusicList() {
        ThreadPool.execute(() -> {
            List<Music> musicList = DBManager.getInstance()
                    .getDaoSession()
                    .queryBuilder(Music.class)
                    .orderDesc(MusicDao.Properties.MusicAddDate)
                    .limit(LIMIT_SIZE).list();
            mMusicList.postValue(musicList);
        });
    }
}
