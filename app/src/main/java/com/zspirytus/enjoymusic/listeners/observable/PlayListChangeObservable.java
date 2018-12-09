package com.zspirytus.enjoymusic.listeners.observable;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayListChangeObservable {

    private List<PlayListChangeObserver> mObservers = new ArrayList<>();
    private List<Music> mLastEvent;

    public void register(PlayListChangeObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
            if (mLastEvent != null) {
                observer.onPlayListChange(mLastEvent);
            }
        }
    }

    public void unregister(PlayListChangeObserver observer) {
        mObservers.remove(observer);
    }

    protected void notifyAllObserverPlayListChange(List<Music> playList) {
        mLastEvent = playList;
        for (PlayListChangeObserver observer : mObservers) {
            observer.onPlayListChange(playList);
        }
    }
}
