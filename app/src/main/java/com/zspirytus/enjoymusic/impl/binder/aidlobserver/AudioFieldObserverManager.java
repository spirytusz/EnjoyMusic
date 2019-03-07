package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.foregroundobserver.IAudioFieldChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.AudioFieldChangeObserver;

import java.util.LinkedList;
import java.util.List;

public class AudioFieldObserverManager extends IAudioFieldChangeObserver.Stub {

    private List<AudioFieldChangeObserver> mObservers;

    private static class Singleton {
        static AudioFieldObserverManager INSTANCE = new AudioFieldObserverManager();
    }

    private AudioFieldObserverManager() {
        mObservers = new LinkedList<>();
    }

    public static AudioFieldObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onChange(int position) throws RemoteException {
        for (AudioFieldChangeObserver observer : mObservers) {
            observer.onChange(position);
        }
    }

    public void register(AudioFieldChangeObserver observer) {
        if (!mObservers.contains(observer)) {
            mObservers.add(observer);
        }
    }

    public void unregister(AudioFieldChangeObserver observer) {
        mObservers.remove(observer);
    }
}
