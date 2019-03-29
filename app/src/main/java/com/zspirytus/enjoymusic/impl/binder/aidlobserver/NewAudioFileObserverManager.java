package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import android.os.RemoteException;

import com.zspirytus.basesdk.utils.LogUtil;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.INewAudioFileObserver;
import com.zspirytus.enjoymusic.receivers.observer.NewAudioFileObserver;

import java.util.LinkedList;
import java.util.List;

public class NewAudioFileObserverManager extends INewAudioFileObserver.Stub {

    private static class Singleton {
        static NewAudioFileObserverManager INSTANCE = new NewAudioFileObserverManager();
    }

    private List<NewAudioFileObserver> mObservers;

    private NewAudioFileObserverManager() {
        mObservers = new LinkedList<>();
    }

    public static NewAudioFileObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void register(NewAudioFileObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void unregister(NewAudioFileObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void onNewMusic(Music music) throws RemoteException {
        LogUtil.e(this.getClass().getSimpleName(), "receive music = " + music.getMusicName());
        for (NewAudioFileObserver observer : mObservers) {
            observer.onNewMusic(music);
        }
    }

    @Override
    public void onNewAlbum(Album album) throws RemoteException {
        LogUtil.e(this.getClass().getSimpleName(), "receive album = " + album.getAlbumName());
        for (NewAudioFileObserver observer : mObservers) {
            observer.onNewAlbum(album);
        }
    }

    @Override
    public void onNewArtist(Artist artist) throws RemoteException {
        LogUtil.e(this.getClass().getSimpleName(), "receive artist = " + artist.getArtistName());
        for (NewAudioFileObserver observer : mObservers) {
            observer.onNewArtist(artist);
        }
    }
}
