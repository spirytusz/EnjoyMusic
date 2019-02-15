package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.MusicFilter;

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
        MusicPlayOrderManager.getInstance().setPlayList(musicFilter);
    }
}
