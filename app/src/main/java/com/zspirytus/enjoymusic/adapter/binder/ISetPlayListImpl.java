package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.cache.MusicScanner;
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
        List<Music> filterMusicList = musicFilter.filter(MusicScanner.getInstance().getAllMusicList());
        MusicPlayOrderManager.getInstance().setPlayList(filterMusicList);
    }
}
