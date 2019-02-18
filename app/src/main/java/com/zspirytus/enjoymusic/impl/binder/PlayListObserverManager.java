package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayListObserverManager extends IPlayListChangeObserver.Stub {

    private MusicFilter mEvent;

    private static class Singleton {
        static PlayListObserverManager INSTANCE = new PlayListObserverManager();
    }

    private List<com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver> observers = new ArrayList<>();

    private PlayListObserverManager() {
    }

    public static PlayListObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onPlayListChange(MusicFilter filter) throws RemoteException {
        mEvent = filter;
        for (com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver observer : observers) {
            observer.onPlayListChanged(filter);
        }
    }

    public void register(com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            if (mEvent != null) {
                observer.onPlayListChanged(mEvent);
            }
        }
    }

    public void unregister(com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver observer) {
        observers.remove(observer);
    }
}
