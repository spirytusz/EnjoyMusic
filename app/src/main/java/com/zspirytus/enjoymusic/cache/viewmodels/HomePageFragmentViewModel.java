package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.NewAudioFileObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.NewAudioFileObserver;

import java.util.List;

public class HomePageFragmentViewModel extends ViewModel implements NewAudioFileObserver {

    private static final int LIMIT_SIZE = 50;
    private MutableLiveData<List<Music>> mMusicList;
    private MutableLiveData<Music> mPlayListFirstMusic;
    private MutableLiveData<Music> mNewMusic;

    public void init() {
        NewAudioFileObserverManager.getInstance().register(this);
        mMusicList = new MutableLiveData<>();
        mPlayListFirstMusic = new MutableLiveData<>();
        mNewMusic = new MutableLiveData<>();
    }

    public MutableLiveData<List<Music>> getMusicList() {
        return mMusicList;
    }

    public MutableLiveData<Music> getPlayListFirstMusic() {
        return mPlayListFirstMusic;
    }

    public MutableLiveData<Music> getNewMusic() {
        return mNewMusic;
    }

    @Override
    public void onNewMusic(Music music) {
        mNewMusic.postValue(music);
    }

    @Override
    public void onNewAlbum(Album album) {
        // do nothing
    }

    @Override
    public void onNewArtist(Artist artist) {
        // do nothing
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        NewAudioFileObserverManager.getInstance().unregister(this);
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
