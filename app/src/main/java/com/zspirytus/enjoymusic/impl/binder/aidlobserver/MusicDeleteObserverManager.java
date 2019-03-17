package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IMusicDeleteObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicDeleteObserver;

import java.util.LinkedList;
import java.util.List;

public class MusicDeleteObserverManager extends IMusicDeleteObserver.Stub {

    private static class Singleton {
        static MusicDeleteObserverManager INSTANCE = new MusicDeleteObserverManager();
    }

    private List<MusicDeleteObserver> observers;

    private MusicDeleteObserverManager() {
        observers = new LinkedList<>();
    }

    public static MusicDeleteObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void register(MusicDeleteObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(MusicDeleteObserver observer) {
        observers.remove(observer);
    }

    private void notifyAllObserver(Music music) {
        for (MusicDeleteObserver observer : observers) {
            observer.onMusicDelete(music);
        }
    }

    @Override
    public void onMusicDelete(Music music) throws RemoteException {
        notifyAllObserver(music);
    }
}
