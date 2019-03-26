package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;

import com.zspirytus.enjoymusic.foregroundobserver.IAudioFieldChangeObserver;

public abstract class PresetReverbObservable {

    protected RemoteCallbackList<IAudioFieldChangeObserver> mRemoteCallbackList = new RemoteCallbackList<>();

    public abstract void register(IAudioFieldChangeObserver observer);

    public abstract void unregister(IAudioFieldChangeObserver observer);

    public abstract void notifyAllObserverAudioFieldChange(int position);
}
