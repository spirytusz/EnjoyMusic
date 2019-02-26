package com.zspirytus.enjoymusic.cache;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.listeners.observable.PlayHistoryObservable;

import java.util.LinkedList;

public class PlayHistoryCache extends PlayHistoryObservable {

    private static final int LIMIT_SIZE = 100;

    private static class SingletonHolder {
        static PlayHistoryCache INSTANCE = new PlayHistoryCache();
    }

    private PlayHistoryCache() {
        mPlayHistory = new LinkedList<>();
    }

    public static PlayHistoryCache getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void add(Music music) {
        int indexOfTargetMusic = mPlayHistory.indexOf(music);
        if (indexOfTargetMusic != -1) {
            mPlayHistory.remove(indexOfTargetMusic);
        }
        if (mPlayHistory.size() == LIMIT_SIZE) {
            mPlayHistory.remove(LIMIT_SIZE - 1);
        }
        mPlayHistory.add(0, music);
        notifyAllObserverPlayListChange();
        MusicSharedPreferences.savePlayHistory(mPlayHistory);
    }

    public Music getPreviousPlayedMusic() {
        if (!mPlayHistory.isEmpty()) {
            int size = mPlayHistory.size();
            if (size >= 2) {
                return mPlayHistory.get(size - 2);
            } else {
                return mPlayHistory.get(size - 1);
            }
        }
        return null;
    }
}
