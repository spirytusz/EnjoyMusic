package com.zspirytus.enjoymusic.engine;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.PlayHistory;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayHistoryChangeObserver;
import com.zspirytus.enjoymusic.listeners.observable.RemoteObservable;

import java.util.List;

public class PlayHistoryManager extends RemoteObservable<IPlayHistoryChangeObserver, List<Music>> {

    private static final int PLAY_HISTORY_LIMIT_SIZE = 100;

    private List<Music> mPlayHistory;

    private static class SingletonHolder {
        static PlayHistoryManager INSTANCE = new PlayHistoryManager();
    }

    private PlayHistoryManager() {
        mPlayHistory = QueryExecutor.getPlayHistory(PLAY_HISTORY_LIMIT_SIZE);
    }

    @Override
    public void register(IPlayHistoryChangeObserver observer) {
        getCallbackList().register(observer);
        try {
            observer.onPlayHistoryChange(mPlayHistory);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister(IPlayHistoryChangeObserver observer) {
        getCallbackList().unregister(observer);
    }

    @Override
    protected void notifyChange(List<Music> musicList) {
        int size = getCallbackList().beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                getCallbackList().getBroadcastItem(i).onPlayHistoryChange(mPlayHistory);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        getCallbackList().finishBroadcast();
    }

    public static PlayHistoryManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(Music music) {
        int index = mPlayHistory.indexOf(music);
        if (index != -1) {
            mPlayHistory.remove(index);
        }
        mPlayHistory.add(0, music);
        notifyChange(mPlayHistory);

        insertToDB(music);
    }

    private void insertToDB(Music music) {
        PlayHistory playHistory = new PlayHistory(music.getMusicId(), System.currentTimeMillis());
        DBManager.getInstance().getDaoSession().getPlayHistoryDao().insertOrReplace(playHistory);
    }
}
