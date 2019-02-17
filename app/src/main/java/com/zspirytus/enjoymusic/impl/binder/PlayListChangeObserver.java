package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayListChangeObserver extends IPlayListChangeObserver.Stub {

    private static class Singleton {
        static PlayListChangeObserver INSTANCE = new PlayListChangeObserver();
    }

    private List<com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver> observers = new ArrayList<>();

    private PlayListChangeObserver() {
    }

    public static PlayListChangeObserver getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onPlayListChange(MusicFilter filter) throws RemoteException {
        for (com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver observer : observers) {
            observer.onPlayListChange(filter);
        }
    }

    public void register(com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver observer) {
        observers.remove(observer);
    }
}
