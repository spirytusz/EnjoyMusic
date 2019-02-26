package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayHistoryChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayHistoryObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayHistoryObserverManager extends IPlayHistoryChangeObserver.Stub {

    private List<Music> mEvent;
    private List<PlayHistoryObserver> mObservers;

    private static class Singleton {
        static PlayHistoryObserverManager INSTANCE = new PlayHistoryObserverManager();
    }

    private PlayHistoryObserverManager() {
        mObservers = new ArrayList<>();
    }

    public static PlayHistoryObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void register(PlayHistoryObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
            if (mEvent != null) {
                observer.onPlayHistoryChange(mEvent);
            }
        }
    }

    public void unregister(PlayHistoryObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void onPlayHistoryChange(List<Music> playHistory) {
        mEvent = playHistory;
        for (PlayHistoryObserver observer : mObservers) {
            observer.onPlayHistoryChange(playHistory);
        }
    }
}
