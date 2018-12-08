package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;

import java.util.List;

public class ISetPlayListImpl extends ISetPlayList.Stub {

    private static class SingletonHolder {
        static ISetPlayListImpl INSTANCE = new ISetPlayListImpl();
    }

    private ISetPlayListImpl() {
    }

    public static ISetPlayListImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void setPlayList(MusicFilter musicFilter) {
        List<Music> filterMusicList = IGetMusicListImpl.getInstance().getMusicList();
        String filterByAlbum = musicFilter.getMusicAlbum();
        String filterByArtist = musicFilter.getMusicArtist();
        if (filterByAlbum != "*" && filterByAlbum != "*") {
            for (int i = filterMusicList.size() - 1; i >= 0; i--) {
                Music music = filterMusicList.get(i);
                if (!music.getMusicAlbumName().equals(filterByAlbum) || !music.getMusicArtist().equals(filterByArtist)) {
                    filterMusicList.remove(music);
                }
            }
        }
        MusicPlayOrderManager.getInstance().setPlayList(filterMusicList);
    }
}
