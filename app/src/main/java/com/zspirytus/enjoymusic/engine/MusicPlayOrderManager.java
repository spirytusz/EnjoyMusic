package com.zspirytus.enjoymusic.engine;

import android.content.Context;
import android.content.SharedPreferences;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.utils.RandomUtil;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayOrderManager {

    public static final int MODE_ORDER = 1;
    public static final int MODE_RANDOM = 2;
    public static final int MODE_LOOP = 4;

    private static final MusicPlayOrderManager INSTANCE = new MusicPlayOrderManager();
    private static final String MODE_FILE_NAME = "play_mode";
    private static final String MODE_KEY = "mode_key";

    private MusicCache mMusicCache = MusicCache.getInstance();
    private List<Music> currentMusicList;

    private int mMode;

    private MusicPlayOrderManager() {
        restoreMode();
        currentMusicList = mMusicCache.getMusicList();
    }

    public static MusicPlayOrderManager getInstance() {
        return INSTANCE;
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public int getMode() {
        return mMode;
    }

    public Music getNextMusic() {
        switch (mMode) {
            case MODE_RANDOM:
                return currentMusicList.get(RandomUtil.rand(currentMusicList.size()));
            case MODE_LOOP:
                return mMusicCache.getCurrentPlayingMusic();
            default:
                int currentPosition = currentMusicList.indexOf(mMusicCache.getCurrentPlayingMusic());
                return currentMusicList.get((currentPosition + 1) % currentMusicList.size());
        }
    }

    public Music getPreviousMusic() {
        switch (mMode) {
            case MODE_RANDOM:
                return currentMusicList.get(RandomUtil.rand(currentMusicList.size()));
            case MODE_LOOP:
                return mMusicCache.getCurrentPlayingMusic();
            default:
                int currentPosition = currentMusicList.indexOf(mMusicCache.getCurrentPlayingMusic());
                int previousPosition = currentPosition - 1 >= 0 ? currentPosition - 1 : currentPosition - 1 + currentMusicList.size();
                return currentMusicList.get(previousPosition);
        }
    }

    private void saveMode() {
        SharedPreferences.Editor editor = MyApplication.getGlobalContext().getSharedPreferences(MODE_FILE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(MODE_KEY, mMode);
        editor.apply();
    }

    private int restoreMode() {
        SharedPreferences pref = MyApplication.getGlobalContext().getSharedPreferences(MODE_FILE_NAME, Context.MODE_PRIVATE);
        int mode = pref.getInt(MODE_KEY, MODE_ORDER);
        return mode;
    }

}
