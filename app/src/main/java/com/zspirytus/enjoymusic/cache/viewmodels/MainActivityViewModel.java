package com.zspirytus.enjoymusic.cache.viewmodels;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.impl.binder.aidlobserver.MusicDeleteObserverManager;
import com.zspirytus.enjoymusic.receivers.observer.MusicDeleteObserver;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends MusicPlayingStateViewModel implements MusicDeleteObserver {


    @Override
    public void init() {
        super.init();
        MusicDeleteObserverManager.getInstance().register(this);
    }

    @Override
    public void onMusicDelete(Music music) {
        List<Music> musicList = getMusicList().getValue();
        if (musicList != null) {
            boolean b = musicList.remove(music);
        }
        getMusicList().postValue(musicList);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        MusicDeleteObserverManager.getInstance().unregister(this);
    }

    public void obtainMusicList() {
        ThreadPool.execute(() -> {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                List<Music> allMusicList = getMusicListBinder.getMusicList();
                List<Album> albumList = getMusicListBinder.getAlbumList();
                List<Artist> artistList = getMusicListBinder.getArtistList();
                List<Folder> folderList = getMusicListBinder.getFolderList();

                getMusicList().postValue(allMusicList);
                getAlbumList().postValue(albumList);
                getArtistList().postValue(artistList);
                getFolderList().postValue(folderList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void obtainEmptyData() {
        getMusicList().setValue(new ArrayList<>());
        getAlbumList().setValue(new ArrayList<>());
        getArtistList().setValue(new ArrayList<>());
        getFolderList().setValue(new ArrayList<>());
    }

    public void applySongLists() {
        List<SongList> songLists = DBManager.getInstance().getDaoSession().getSongListDao().loadAll();
        setSongList(songLists);
    }

    void updateArtist(Artist needUpdateArtist) {
        List<Artist> artistList = getArtistList().getValue();
        if (artistList == null) {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                artistList = getMusicListBinder.getArtistList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < artistList.size(); i++) {
            if (needUpdateArtist.getArtistId().equals(artistList.get(i).getArtistId())) {
                artistList.get(i).setArtistArt(needUpdateArtist.peakArtistArt());
                break;
            }
        }
        getArtistList().postValue(artistList);
    }

    void updateAlbum(Album album) {
        List<Album> albumList = getAlbumList().getValue();
        if (albumList == null) {
            try {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.GET_MUSIC_LIST);
                IGetMusicList getMusicListBinder = IGetMusicList.Stub.asInterface(binder);
                albumList = getMusicListBinder.getAlbumList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < albumList.size(); i++) {
            if (album.getAlbumId().equals(albumList.get(i).getAlbumId())) {
                albumList.set(i, album);
                break;
            }
        }
        getAlbumList().postValue(albumList);
        getMusicList().postValue(getMusicList().getValue());

        // force refresh currentPlayingMusic when its album equal to needUpdateAlbum.
        Music currentPlayMusic = getCurrentPlayingMusic().getValue();
        if (currentPlayMusic != null && currentPlayMusic.getAlbumId().equals(album.getAlbumId())) {
            getCurrentPlayingMusic().postValue(getCurrentPlayingMusic().getValue());
        }
    }
}
