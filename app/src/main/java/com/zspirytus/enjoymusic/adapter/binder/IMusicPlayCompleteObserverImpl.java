package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.IMusicPlayCompleteObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayCompleteObserver;

import java.util.ArrayList;
import java.util.List;

public class IMusicPlayCompleteObserverImpl extends IMusicPlayCompleteObserver.Stub {

    private static class SingletonHolder {
        static IMusicPlayCompleteObserverImpl INSTANCE = new IMusicPlayCompleteObserverImpl();
    }

    private List<MusicPlayCompleteObserver> observers = new ArrayList<>();

    private IMusicPlayCompleteObserverImpl() {
    }

    public static IMusicPlayCompleteObserverImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onMusicPlayComplete() {
        for (MusicPlayCompleteObserver observer : observers) {
            observer.onMusicPlayComplete();
        }
    }

    public void register(MusicPlayCompleteObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(MusicPlayCompleteObserver observer) {
        observers.remove(observer);
    }
}
