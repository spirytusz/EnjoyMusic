package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicMetaDataUpdator;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;

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
    public void updateAlbum(Album album) throws RemoteException {
        DBManager.getInstance().getDaoSession().getAlbumDao().insertOrReplace(album);
    }
}
