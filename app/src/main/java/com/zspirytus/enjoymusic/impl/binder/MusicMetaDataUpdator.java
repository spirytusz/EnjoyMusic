package com.zspirytus.enjoymusic.impl.binder;

import android.content.ContentUris;
import android.net.Uri;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.provider.MediaStore;

import com.zspirytus.enjoymusic.IMusicMetaDataUpdator;
import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.db.table.CustomAlbumArt;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IMusicDeleteObserver;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class MusicMetaDataUpdator extends IMusicMetaDataUpdator.Stub {

    private static class Singleton {
        static MusicMetaDataUpdator INSTANCE = new MusicMetaDataUpdator();
    }

    private RemoteCallbackList<IMusicDeleteObserver> observers;

    private MusicMetaDataUpdator() {
        observers = new RemoteCallbackList<>();
    }

    public static MusicMetaDataUpdator getInstance() {
        return Singleton.INSTANCE;
    }

    public void register(IMusicDeleteObserver observer) {
        observers.register(observer);
    }

    public void unregister(IMusicDeleteObserver observer) {
        observers.unregister(observer);
    }

    private void notifyAllObserver(Music music) {
        int size = observers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                observers.getBroadcastItem(i).onMusicDelete(music);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        observers.finishBroadcast();
    }

    @Override
    public void updateArtist(Artist artist) {
        ArtistArt artistArt = artist.peakArtistArt();
        DBManager.getInstance().getDaoSession().getArtistArtDao().insertOrReplace(artistArt);
    }

    @Override
    public boolean updateAlbum(Album album) {
        String picUrl = album.getAlbumArt();
        try {
            File file = GlideApp.with(MainApplication.getAppContext()).asFile().load(picUrl).submit().get();
            CustomAlbumArt customAlbumArt = new CustomAlbumArt(album.getAlbumId(), file.getAbsolutePath());
            DBManager.getInstance().getDaoSession().getCustomAlbumArtDao().insertOrReplace(customAlbumArt);
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean deleteMusic(Music music) {
        File file = new File(music.getMusicFilePath());
        if (file.exists()) {
            Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, music.getMusicId());
            int i = MainApplication.getAppContext().getContentResolver().delete(
                    uri,
                    null,
                    null
            );
            if (i > 0) {
                file.delete();
                DBManager.getInstance().getDaoSession().getMusicDao().delete(music);
                MusicScanner.getInstance().getAllMusicList().remove(music);
                notifyAllObserver(music);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
