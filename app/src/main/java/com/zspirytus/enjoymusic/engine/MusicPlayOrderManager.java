package com.zspirytus.enjoymusic.engine;

import android.content.Context;
import android.content.SharedPreferences;

import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
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

    private List<Music> currentMusicList;

    private int mMode;

    private MusicPlayOrderManager() {
        restoreMode();
        setMode(MODE_ORDER);
    }

    public static MusicPlayOrderManager getInstance() {
        return INSTANCE;
    }

    public void init() {
        currentMusicList = AllMusicCache.getInstance().getAllMusicListWithoutScanning();
    }

    public void setMode(int mode) {
        mMode = mode;
    }

    public int getMode() {
        return mMode;
    }

    public Music getNextMusic() {
        Music nextMusic;
        switch (mMode) {
            case MODE_RANDOM:
                nextMusic = currentMusicList.get(RandomUtil.rand(currentMusicList.size()));
                break;
            case MODE_LOOP:
                nextMusic = CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic();
                break;
            default:
                int currentPosition = currentMusicList.indexOf(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
                int nextPosition = (currentPosition + 1) % currentMusicList.size();
                nextMusic = currentMusicList.get(nextPosition);
                break;
        }
        return nextMusic != null ? nextMusic : currentMusicList.get(0);
    }

    public Music getPreviousMusic() {
        Music previousMusic;
        switch (mMode) {
            case MODE_RANDOM:
                previousMusic = currentMusicList.get(RandomUtil.rand(currentMusicList.size()));
                break;
            case MODE_LOOP:
                previousMusic = CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic();
                break;
            default:
                int currentPosition = currentMusicList.indexOf(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
                int previousPosition = currentPosition - 1 >= 0 ? currentPosition - 1 : currentPosition - 1 + currentMusicList.size();
                previousMusic = currentMusicList.get(previousPosition);
                break;
        }
        return previousMusic != null ? previousMusic : currentMusicList.get(0);
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
