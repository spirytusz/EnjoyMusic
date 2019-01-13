package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.List;

public class IGetMusicListImpl extends IGetMusicList.Stub {

    private static class SingletonHolder {
        static IGetMusicListImpl INSTANCE = new IGetMusicListImpl();
    }

    private IGetMusicListImpl() {
    }

    public static IGetMusicListImpl getInstance() {
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
