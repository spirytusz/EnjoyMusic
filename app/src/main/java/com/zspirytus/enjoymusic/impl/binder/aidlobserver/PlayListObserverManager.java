package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayListObserverManager extends IPlayListChangeObserver.Stub {

    private List<Music> mEvent1;

    private static class Singleton {
        static PlayListObserverManager INSTANCE = new PlayListObserverManager();
    }

    private List<PlayListChangeObserver> observers = new ArrayList<>();

    private PlayListObserverManager() {
    }

    public static PlayListObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onPlayListChange(List<Music> playList) {
        mEvent1 = playList;
        for (PlayListChangeObserver observer : observers) {
            observer.onPlayListChangeDirectly(playList);
        }
    }

    public void register(PlayListChangeObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            if (mEvent1 != null) {
                observer.onPlayListChangeDirectly(mEvent1);
            }
        }
    }

    public void unregister(PlayListChangeObserver observer) {
        observers.remove(observer);
    }
}
