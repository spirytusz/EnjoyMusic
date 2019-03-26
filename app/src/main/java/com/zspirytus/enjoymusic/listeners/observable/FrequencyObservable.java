package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;

import com.zspirytus.enjoymusic.foregroundobserver.IFrequencyObserver;

public abstract class FrequencyObservable {

    protected RemoteCallbackList<IFrequencyObserver> callbackList = new RemoteCallbackList<>();

    public abstract void register(IFrequencyObserver observer);

    public abstract void unregister(IFrequencyObserver observer);

    public abstract void notifyAllObserverFrequencyChange(float[] magnitudes, float[] phases);
}
