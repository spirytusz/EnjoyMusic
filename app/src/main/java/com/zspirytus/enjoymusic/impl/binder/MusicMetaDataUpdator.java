package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicMetaDataUpdator;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.db.table.CustomAlbumArt;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class MusicMetaDataUpdator extends IMusicMetaDataUpdator.Stub {

    private static class Singleton {
        static MusicMetaDataUpdator INSTANCE = new MusicMetaDataUpdator();
    }

    private MusicMetaDataUpdator() {
    }

    public static MusicMetaDataUpdator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void updateArtist(Artist artist) {
        ArtistArt artistArt = artist.peakArtistArt();
        DBManager.getInstance().getDaoSession().getArtistArtDao().insertOrReplace(artistArt);
    }

    @Override
    public boolean updateAlbum(Album album) throws RemoteException {
        String picUrl = album.getAlbumArt();
        if (picUrl.contains("http") || picUrl.contains("https")) {
            try {
                File file = GlideApp.with(MainApplication.getBackgroundContext()).asFile().load(picUrl).submit().get();
                CustomAlbumArt customAlbumArt = new CustomAlbumArt(album.getAlbumId(), file.getAbsolutePath());
                DBManager.getInstance().getDaoSession().getCustomAlbumArtDao().insertOrReplace(customAlbumArt);
                return true;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
