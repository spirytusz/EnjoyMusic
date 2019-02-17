package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.foregroundobserver.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayProgressChangeObserver extends IPlayProgressChangeObserver.Stub {

    private static class SingletonHolder {
        static PlayProgressChangeObserver INSTANCE = new PlayProgressChangeObserver();
    }

    private PlayProgressChangeObserver() {
    }

    private List<MusicPlayProgressObserver> observers = new ArrayList<>();

    @Override
    public void onProgressChange(int milliseconds) {
        for (MusicPlayProgressObserver observer : observers) {
            observer.onProgressChanged(milliseconds);
        }
    }

    public void register(MusicPlayProgressObserver observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void unregister(MusicPlayProgressObserver observer) {
        observers.remove(observer);
    }

    public static PlayProgressChangeObserver getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
