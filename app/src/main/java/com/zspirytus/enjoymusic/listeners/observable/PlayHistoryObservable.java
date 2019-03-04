package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayHistoryChangeObserver;

import java.util.List;

public class PlayHistoryObservable {

    private static final int PLAY_HISTORY_LIMIT_SIZE = 100;

    private RemoteCallbackList<IPlayHistoryChangeObserver> mObservers = new RemoteCallbackList<>();
    protected List<Music> mPlayHistory;
    protected static final long PLAY_HISTORY_PRIMARY_KEY = 6666;

    public void register(IPlayHistoryChangeObserver observer) {
        mObservers.register(observer);
        if (mPlayHistory == null) {
            mPlayHistory = QueryExecutor.getPlayHistory(PLAY_HISTORY_LIMIT_SIZE);
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
