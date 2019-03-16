package com.zspirytus.enjoymusic.engine;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.PlayHistory;
import com.zspirytus.enjoymusic.db.table.jointable.JoinPlayHistoryToMusic;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayHistoryChangeObserver;
import com.zspirytus.enjoymusic.listeners.observable.PlayHistoryObservable;

import java.util.List;

public class PlayHistoryManager extends PlayHistoryObservable {

    private static final long PLAY_HISTORY_PRIMARY_KEY = 6666;
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
        mObservers.register(observer);
        try {
            observer.onPlayHistoryChange(mPlayHistory);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister(IPlayHistoryChangeObserver observer) {
        mObservers.unregister(observer);
    }

    @Override
    protected void notifyAllObserverPlayListChange() {
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

    public static PlayHistoryManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(Music music) {
        int index = mPlayHistory.indexOf(music);
        if (index != -1) {
            mPlayHistory.remove(index);
        }
        mPlayHistory.add(0, music);
        notifyAllObserverPlayListChange();

        insertToDB(music);
    }

    private void insertToDB(Music music) {
        PlayHistory playHistory = DBManager.getInstance().getDaoSession().load(PlayHistory.class, PLAY_HISTORY_PRIMARY_KEY);
        if (playHistory == null) {
            DBManager.getInstance().getDaoSession().getPlayHistoryDao().insert(new PlayHistory(PLAY_HISTORY_PRIMARY_KEY));
        }
        JoinPlayHistoryToMusic joinPlayHistoryToMusic = new JoinPlayHistoryToMusic(PLAY_HISTORY_PRIMARY_KEY, music.getMusicId(), System.currentTimeMillis());
        DBManager.getInstance().getDaoSession().getJoinPlayHistoryToMusicDao().insertOrReplace(joinPlayHistoryToMusic);
    }
}
