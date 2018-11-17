package com.zspirytus.enjoymusic.adapter.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;

import java.util.ArrayList;
import java.util.List;

public class IPlayProgressChangeObserverImpl extends IPlayProgressChangeObserver.Stub {

    private static class SingletonHolder {
        static IPlayProgressChangeObserverImpl INSTANCE = new IPlayProgressChangeObserverImpl();
    }

    private List<MusicPlayProgressObserver> observers = new ArrayList<>();

    @Override
    public void onProgressChange(int progress) throws RemoteException {
        for (MusicPlayProgressObserver observer : observers) {
            observer.onProgressChanged(progress);
        }
    }

    public void register(MusicPlayProgressObserver observer) {
        if (!observers.contains(observer))
            observers.add(observer);
    }

    public void unregister(MusicPlayProgressObserver observer) {
        observers.remove(observer);
    }

    public static IPlayProgressChangeObserverImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
