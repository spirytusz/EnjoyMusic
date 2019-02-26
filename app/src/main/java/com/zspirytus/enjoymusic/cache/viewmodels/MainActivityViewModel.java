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
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.global.MainApplication;

import java.util.List;

public class MainActivityViewModel extends MusicPlayingStateViewModel {

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
                obtainMusicDataFromPref(MainApplication.getForegroundContext());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void obtainMusicDataFromPref(Context context) {
        Music savedMusic = MusicSharedPreferences.restoreMusic(context);
        if (savedMusic != null) {
            postCurrentPlayingMusic(savedMusic);
        }
    }

    public void applySongLists() {
        List<SongList> songLists = DBManager.getInstance().getDaoSession().getSongListDao().loadAll();
        setSongList(songLists);
    }

}
