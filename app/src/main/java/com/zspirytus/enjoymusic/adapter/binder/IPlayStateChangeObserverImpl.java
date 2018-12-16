package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;

import java.util.ArrayList;
import java.util.List;

public class IPlayStateChangeObserverImpl extends IPlayStateChangeObserver.Stub {

    private Boolean mEvent;

    private static class SingletonHolder {
        static IPlayStateChangeObserverImpl INSTANCE = new IPlayStateChangeObserverImpl();
    }

    private List<MusicPlayStateObserver> observers = new ArrayList<>();

    private IPlayStateChangeObserverImpl() {
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
            if (mEvent != null) {
                observer.onPlayingStateChanged(mEvent);
            }
        }
    }

    public void unregister(MusicPlayStateObserver observer) {
        observers.remove(observer);
    }

    public static IPlayStateChangeObserverImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
