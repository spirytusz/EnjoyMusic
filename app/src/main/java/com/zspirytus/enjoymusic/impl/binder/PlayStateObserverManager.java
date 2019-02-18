package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayStateObserverManager extends IPlayStateChangeObserver.Stub {

    private boolean mEvent = false;

    private static class SingletonHolder {
        static PlayStateObserverManager INSTANCE = new PlayStateObserverManager();
    }

    private List<MusicPlayStateObserver> observers = new ArrayList<>();

    private PlayStateObserverManager() {
    }

    @Override
    public void onPlayStateChange(boolean isPlaying) {
        mEvent = isPlaying;
        for (MusicPlayStateObserver observer : observers) {
            observer.onPlayingStateChanged(isPlaying);
        }
    }

    public void register(MusicPlayStateObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            observer.onPlayingStateChanged(mEvent);
        }
    }

    public void unregister(MusicPlayStateObserver observer) {
        observers.remove(observer);
    }

    public static PlayStateObserverManager getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
