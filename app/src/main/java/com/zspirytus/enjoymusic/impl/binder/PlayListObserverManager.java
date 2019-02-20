package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayListObserverManager extends IPlayListChangeObserver.Stub {

    private MusicFilter mEvent;
    private List<Music> mEvent1;

    private static class Singleton {
        static PlayListObserverManager INSTANCE = new PlayListObserverManager();
    }

    private List<com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver> observers = new ArrayList<>();
    private List<PlayListChangeDirectlyObserver> observers1 = new ArrayList<>();

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

    @Override
    public void onPlayListChangeDirectly(List<Music> playList) throws RemoteException {
        mEvent1 = playList;
        for (com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver observer : observers1) {
            observer.onPlayListChangeDirectly(playList);
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

    public void register(PlayListChangeDirectlyObserver observer) {
        if (!observers1.contains(observer)) {
            observers1.add(observer);
            if (mEvent1 != null) {
                observer.onPlayListChangeDirectly(mEvent1);
            }
        }
    }

    public void unregister(PlayListChangeDirectlyObserver observer) {
        observers1.remove(observer);
    }
}
