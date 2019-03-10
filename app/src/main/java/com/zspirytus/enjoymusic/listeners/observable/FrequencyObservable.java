package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.foregroundobserver.IFrequencyObserver;

public class FrequencyObservable {

    private RemoteCallbackList<IFrequencyObserver> callbackList = new RemoteCallbackList<>();

    public void register(IFrequencyObserver observer) {
        callbackList.register(observer);
    }

    public void unregister(IFrequencyObserver observer) {
        callbackList.unregister(observer);
    }

    public void notifyAllObserverFrequencyChange(float[] magnitudes, float[] phases) {
        int size = callbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                callbackList.getBroadcastItem(i).onFrequencyChange(magnitudes, phases);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }
}
