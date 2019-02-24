package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;

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
    public void setPlayList(List<Music> playList) throws RemoteException {
        MusicPlayOrderManager.getInstance().setPlayList(playList);
    }

    @Override
    public void appendMusicList(List<Music> musicList) throws RemoteException {
        MusicPlayOrderManager.getInstance().addMusicListToPlayList(musicList);
    }
}
