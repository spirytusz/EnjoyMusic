package com.zspirytus.enjoymusic.cache.viewmodels;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.impl.binder.PlayMusicChangeObserver;
import com.zspirytus.enjoymusic.impl.binder.PlayStateChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivityViewModel extends MusicDataViewModel implements PlayedMusicChangeObserver,
        MusicPlayStateObserver, PlayListChangeObserver {

    public void init() {
        super.init();
        PlayMusicChangeObserver.getInstance().register(this);
        PlayStateChangeObserver.getInstance().register(this);
        com.zspirytus.enjoymusic.impl.binder.PlayListChangeObserver.getInstance().register(this);
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
        final List<Music> fiterMusicList = filter.filter(getMusicList().getValue());
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            setPlayList(fiterMusicList);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ForegroundMusicController.getInstance().release();
        PlayMusicChangeObserver.getInstance().unregister(this);
        PlayStateChangeObserver.getInstance().unregister(this);
        com.zspirytus.enjoymusic.impl.binder.PlayListChangeObserver.getInstance().unregister(this);
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
                IBinder iBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                ISetPlayList setPlayList = ISetPlayList.Stub.asInterface(iBinder);
                setPlayList.setPlayList(MusicFilter.NO_FILTER);
                AndroidSchedulers.mainThread().scheduleDirect(() -> {
                    setMusicList(musicList);
                    setAlbumList(albumList);
                    setArtistList(artistList);
                    setFolderList(folderSortedMusicList);
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

}
