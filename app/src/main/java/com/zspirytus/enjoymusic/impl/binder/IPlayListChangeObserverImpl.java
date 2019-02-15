package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class IPlayListChangeObserverImpl extends IPlayListChangeObserver.Stub {

    private static class Singleton {
        static IPlayListChangeObserverImpl INSTANCE = new IPlayListChangeObserverImpl();
    }

    private List<PlayListChangeObserver> observers = new ArrayList<>();

    private IPlayListChangeObserverImpl() {
    }

    public static IPlayListChangeObserverImpl getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onPlayListChange(MusicFilter filter) throws RemoteException {
        for (PlayListChangeObserver observer : observers) {
            observer.onPlayListChange(filter);
        }
    }

    public void register(PlayListChangeObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(PlayListChangeObserver observer) {
        observers.remove(observer);
    }
}
