package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;
import com.zspirytus.enjoymusic.global.MainApplication;

import java.util.List;

public class PlayListChangeObservable {

    protected List<Music> mPlayList;

    private RemoteCallbackList<IPlayListChangeObserver> mObservers = new RemoteCallbackList<>();

    public void register(IPlayListChangeObserver observer) {
        mObservers.register(observer);
        if (mPlayList == null) {
            mPlayList = MusicSharedPreferences.restorePlayList(MainApplication.getBackgroundContext());
        }
        try {
            observer.onPlayListChange(mPlayList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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
