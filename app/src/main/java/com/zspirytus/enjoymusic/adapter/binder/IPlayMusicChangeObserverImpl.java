package com.zspirytus.enjoymusic.adapter.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IPlayMusicChangeObserver;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class IPlayMusicChangeObserverImpl extends IPlayMusicChangeObserver.Stub {

    private static class SingletonHolder {
        static IPlayMusicChangeObserverImpl INSTANCE = new IPlayMusicChangeObserverImpl();
    }

    private List<PlayedMusicChangeObserver> observers = new ArrayList<>();

    @Override
    public void onMusicChange(Music music) throws RemoteException {
        for (PlayedMusicChangeObserver observer : observers) {
            observer.onPlayedMusicChanged(music);
        }
    }

    public void register(PlayedMusicChangeObserver observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void unregister(PlayedMusicChangeObserver observer) {
        observers.remove(observer);
    }

    public static IPlayMusicChangeObserverImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
