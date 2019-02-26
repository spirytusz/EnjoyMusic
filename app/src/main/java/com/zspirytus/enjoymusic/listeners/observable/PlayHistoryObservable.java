package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayHistoryChangeObserver;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.LogUtil;

import java.util.List;

public class PlayHistoryObservable {

    protected List<Music> mPlayHistory;

    private RemoteCallbackList<IPlayHistoryChangeObserver> mObservers = new RemoteCallbackList<>();

    public void register(IPlayHistoryChangeObserver observer) {
        mObservers.register(observer);
        if (mPlayHistory == null) {
            mPlayHistory = MusicSharedPreferences.restorePlayHistory(MainApplication.getBackgroundContext());
            LogUtil.e(PlayHistoryObservable.class.getSimpleName(), "restorePlayHistorySize = " + mPlayHistory.size());
        }
        try {
            observer.onPlayHistoryChange(mPlayHistory);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregister(IPlayHistoryChangeObserver observer) {
        mObservers.unregister(observer);
    }

    public void notifyAllObserverPlayListChange() {
        int size = mObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mObservers.getBroadcastItem(i).onPlayHistoryChange(mPlayHistory);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mObservers.finishBroadcast();
    }

}
