package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.PlayHistory;
import com.zspirytus.enjoymusic.db.table.jointable.JoinPlayHistoryToMusic;
import com.zspirytus.enjoymusic.listeners.observable.PlayHistoryObservable;

import java.util.LinkedList;

public class PlayHistoryManager extends PlayHistoryObservable {

    private static class SingletonHolder {
        static PlayHistoryManager INSTANCE = new PlayHistoryManager();
    }

    private PlayHistoryManager() {
        mPlayHistory = new LinkedList<>();
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
