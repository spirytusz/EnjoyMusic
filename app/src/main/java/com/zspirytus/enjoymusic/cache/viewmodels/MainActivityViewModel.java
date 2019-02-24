package com.zspirytus.enjoymusic.cache.viewmodels;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.binder.PlayMusicObserverManager;
import com.zspirytus.enjoymusic.impl.binder.PlayStateObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivityViewModel extends MusicDataViewModel implements PlayedMusicChangeObserver,
        MusicPlayStateObserver {

    public void init() {
        super.init();
        PlayMusicObserverManager.getInstance().register(this);
        PlayStateObserverManager.getInstance().register(this);
        setPlayList(MusicSharedPreferences.restorePlayList(MainApplication.getForegroundContext()));
    }

    @Override
    public void onPlayedMusicChanged(final Music music) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            if (music != null) {
                setCurrentPlayingMusic(music);
            }
        });
    }

    @Override
    public void onPlayingStateChanged(final boolean isPlaying) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            setMusicPlayState(isPlaying);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ForegroundMusicController.getInstance().release();
        PlayMusicObserverManager.getInstance().unregister(this);
        PlayStateObserverManager.getInstance().unregister(this);
    }

    public void obtainMusicList() {
        ThreadPool.execute(() -> {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                List<Music> allMusicList = getMusicListBinder.getMusicList();
                List<Album> albumList = getMusicListBinder.getAlbumList();
                List<Artist> artistList = getMusicListBinder.getArtistList();
                List<FolderSortedMusic> folderSortedMusicList = getMusicListBinder.getFolderSortedMusic();

                getMusicList().postValue(allMusicList);
                getAlbumList().postValue(albumList);
                getArtistList().postValue(artistList);
                getFolderList().postValue(folderSortedMusicList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void obtainMusicDataFromPref(Context context) {
        Music savedMusic = MusicSharedPreferences.restoreMusic(context);
        if (savedMusic != null) {
            setCurrentPlayingMusic(savedMusic);
        }
    }

    public void applySongLists() {
        List<SongList> songLists = DBManager.getInstance().getDaoSession().getSongListDao().loadAll();
        setSongList(songLists);
    }

}
