package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayListObserverManager extends IPlayListChangeObserver.Stub {

    private List<Music> mEvent1;

    private static class Singleton {
        static PlayListObserverManager INSTANCE = new PlayListObserverManager();
    }

    private List<PlayListChangeDirectlyObserver> observers = new ArrayList<>();

    private PlayListObserverManager() {
    }

    public static PlayListObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onPlayListChange(List<Music> playList) throws RemoteException {
        mEvent1 = playList;
        for (com.zspirytus.enjoymusic.receivers.observer.PlayListChangeDirectlyObserver observer : observers) {
            observer.onPlayListChangeDirectly(playList);
        }
    }

    public void register(PlayListChangeDirectlyObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            if (mEvent1 != null) {
                observer.onPlayListChangeDirectly(mEvent1);
            }
        }
    }

    public void unregister(PlayListChangeDirectlyObserver observer) {
        observers.remove(observer);
    }
}
