package com.zspirytus.enjoymusic.cache.viewmodels;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.entity.convert.Convertor;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.binder.PlayListObserverManager;
import com.zspirytus.enjoymusic.impl.binder.PlayMusicObserverManager;
import com.zspirytus.enjoymusic.impl.binder.PlayStateObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivityViewModel extends MusicDataViewModel implements PlayedMusicChangeObserver,
        MusicPlayStateObserver, PlayListChangeObserver {

    public void init() {
        super.init();
        PlayMusicObserverManager.getInstance().register(this);
        PlayStateObserverManager.getInstance().register(this);
        PlayListObserverManager.getInstance().register(this);
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
    public void onPlayListChanged(MusicFilter filter) {
        List<Music> musicList = getMusicList().getValue();
        if (musicList != null) {
            final List<Music> fiterMusicList = filter.filter(getMusicList().getValue());
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                setPlayList(fiterMusicList);
            });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ForegroundMusicController.getInstance().release();
        PlayMusicObserverManager.getInstance().unregister(this);
        PlayStateObserverManager.getInstance().unregister(this);
        PlayListObserverManager.getInstance().unregister(this);
    }

    public void obtainMusicList() {
        ThreadPool.execute(() -> {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                final List<Music> musicList = getMusicListBinder.getMusicList();
                final List<Album> albumList = getMusicListBinder.getAlbumList();
                final List<Artist> artistList = getMusicListBinder.getArtistList();
                final List<FolderSortedMusic> folderSortedMusicList = getMusicListBinder.getFolderSortedMusic();
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    setMusicList(musicList);
                    setAlbumList(albumList);
                    setArtistList(artistList);
                    setFolderList(folderSortedMusicList);
                });
                // 直接从系统数据库复制Music数据到db_enjoymusic中
                DBManager.getInstance().getDaoSession().getSongDao().insertOrReplaceInTx(Convertor.createSongs(musicList));
                DBManager.getInstance().getDaoSession().getAlbumTableDao().insertOrReplaceInTx(Convertor.createAlbumTables(albumList));
                DBManager.getInstance().getDaoSession().getArtistTableDao().insertOrReplaceInTx(Convertor.createAristTables(artistList));
                DBManager.getInstance().getDaoSession().getJoinArtistToAlbumDao().insertOrReplaceInTx(Convertor.createJoinArtistToAlbums(musicList));
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
