package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.greendao.SongDao;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.convert.Convertor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class HomePageFragmentViewModel extends ViewModel {

    private static final int LIMIT_SIZE = 30;
    private MutableLiveData<List<Music>> mMusicList = new MutableLiveData<>();

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicList;
    }

    public void applyMusicList() {
        ThreadPool.execute(() -> {
            List<Song> songs = DBManager.getInstance()
                    .getDaoSession()
                    .queryBuilder(Song.class)
                    .orderDesc(SongDao.Properties.MusicAddDate)
                    .limit(LIMIT_SIZE).list();
            List<Music> musicList = new ArrayList<>();
            for (Song song : songs) {
                musicList.add(Convertor.createMusic(song));
            }
            AndroidSchedulers.mainThread().scheduleDirect(() -> mMusicList.setValue(musicList));
        });
    }
}
