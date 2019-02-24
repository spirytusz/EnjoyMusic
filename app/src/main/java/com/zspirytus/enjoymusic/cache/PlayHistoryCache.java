package com.zspirytus.enjoymusic.cache;

import com.zspirytus.enjoymusic.db.table.Music;

import java.util.ArrayList;
import java.util.List;

public class PlayHistoryCache {

    private static final int LIMIT_SIZE = 100;

    private List<Music> mPlayHistory;

    private static class SingletonHolder {
        static PlayHistoryCache INSTANCE = new PlayHistoryCache();
    }

    private PlayHistoryCache() {
        mPlayHistory = new ArrayList<>(LIMIT_SIZE);
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
            mPlayHistory.remove(0);
        }
        mPlayHistory.add(music);
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
