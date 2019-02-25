package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.IMusicMetaDataUpdator;
import com.zspirytus.enjoymusic.db.EntityUpdator;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;

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
    public void updateAlbum(Album album) {
        EntityUpdator.updateAlbum(album);
    }

    @Override
    public void updateArtist(Artist artist) {
        EntityUpdator.updateArtist(artist);
    }
}
