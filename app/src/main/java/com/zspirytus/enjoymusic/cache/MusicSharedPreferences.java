package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.zspirytus.enjoymusic.global.MainApplication;

public class MusicSharedPreferences {

    private static final String TAG = "MusicSharedPreferences";

    private static final String PLAY_MODE_KEY = "playModeKey";

    private static final int DEFAULT_PLAY_MODE = 0;

    private MusicSharedPreferences() {
    }

    // restore music playMode will in both foreground and background when init
    public static int restorePlayMode() {
        SharedPreferences pref = MainApplication.getAppContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return pref.getInt(PLAY_MODE_KEY, DEFAULT_PLAY_MODE);
    }

    // save playMode
    public static void savePlayMode(int playMode) {
        SharedPreferences.Editor editor = MainApplication.getAppContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putInt(PLAY_MODE_KEY, playMode);
        editor.apply();
    }
}
