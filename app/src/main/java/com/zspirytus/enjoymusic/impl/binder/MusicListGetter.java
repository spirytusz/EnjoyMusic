package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;

import java.util.List;

public class MusicListGetter extends IGetMusicList.Stub {

    private static class SingletonHolder {
        static MusicListGetter INSTANCE = new MusicListGetter();
    }

    private MusicListGetter() {
    }

    public static MusicListGetter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public List<Music> getMusicList() {
        return MusicScanner.getInstance().getAllMusicList();
    }

    @Override
    public List<Album> getAlbumList() {
        return MusicScanner.getInstance().getAlbumList();
    }

    @Override
    public List<Artist> getArtistList() {
        return MusicScanner.getInstance().getArtistList();
    }

    @Override
    public List<FolderSortedMusic> getFolderSortedMusic() {
        return MusicScanner.getInstance().getFolderSortedMusicList();
    }
}
