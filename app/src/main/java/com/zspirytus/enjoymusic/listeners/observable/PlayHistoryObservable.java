package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;

import com.zspirytus.enjoymusic.foregroundobserver.IPlayHistoryChangeObserver;

public abstract class PlayHistoryObservable {

    protected RemoteCallbackList<IPlayHistoryChangeObserver> mObservers = new RemoteCallbackList<>();

    public abstract void register(IPlayHistoryChangeObserver observer);

    public abstract void unregister(IPlayHistoryChangeObserver observer);

    protected abstract void notifyAllObserverPlayListChange();

}
