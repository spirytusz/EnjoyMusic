package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;

import java.util.List;

public class PlayListChangeObservable {

    private RemoteCallbackList<IPlayListChangeObserver> mObservers = new RemoteCallbackList<>();

    public void register(IPlayListChangeObserver observer) {
        mObservers.register(observer);
    }

    public void unregister(IPlayListChangeObserver observer) {
        mObservers.unregister(observer);
    }

    public void notifyAllObserverPlayListChange(List<Music> playList) {
        int size = mObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mObservers.getBroadcastItem(i).onPlayListChange(playList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mObservers.finishBroadcast();
    }
}
