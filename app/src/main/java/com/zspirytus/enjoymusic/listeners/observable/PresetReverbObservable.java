package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.foregroundobserver.IAudioFieldChangeObserver;

public class PresetReverbObservable {

    private static RemoteCallbackList<IAudioFieldChangeObserver> mRemoteCallbackList = new RemoteCallbackList<>();

    public static void register(IAudioFieldChangeObserver observer) {
        mRemoteCallbackList.register(observer);
    }

    public static void unregister(IAudioFieldChangeObserver observer) {
        mRemoteCallbackList.unregister(observer);
    }

    public static void notifyAllObserverAudioFieldChange(int position) {
        int size = mRemoteCallbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mRemoteCallbackList.getBroadcastItem(i).onChange(position);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mRemoteCallbackList.finishBroadcast();
    }
}
