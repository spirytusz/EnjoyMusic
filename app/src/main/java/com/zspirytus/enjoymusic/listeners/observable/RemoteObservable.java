package com.zspirytus.enjoymusic.listeners.observable;

import android.os.IInterface;
import android.os.RemoteCallbackList;

public abstract class RemoteObservable<C extends IInterface, E> {

    private RemoteCallbackList<C> callbackList = new RemoteCallbackList<>();

    public abstract void register(C callback);

    public abstract void unregister(C callback);

    protected abstract void notifyChange(E e);

    protected RemoteCallbackList<C> getCallbackList() {
        return callbackList;
    }
}
