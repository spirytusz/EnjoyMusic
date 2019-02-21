package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;

import java.util.List;

public class PlayListSetter extends ISetPlayList.Stub {

    private static class SingletonHolder {
        static PlayListSetter INSTANCE = new PlayListSetter();
    }

    private PlayListSetter() {
    }

    public static PlayListSetter getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void setPlayList(MusicFilter musicFilter) {
        MusicPlayOrderManager.getInstance().setPlayList(musicFilter);
    }

    @Override
    public void setPlayListDirectly(List<Music> playList) throws RemoteException {
        MusicPlayOrderManager.getInstance().setPlayList(playList);
    }

    @Override
    public void appendMusicListDirectly(List<Music> musicList) throws RemoteException {
        MusicPlayOrderManager.getInstance().addMusicListToPlayList(musicList);
    }

    @Override
    public void appendMusic(MusicFilter musicFilter) throws RemoteException {
        MusicPlayOrderManager.getInstance().addMusicToPlayList(musicFilter);
    }
}
