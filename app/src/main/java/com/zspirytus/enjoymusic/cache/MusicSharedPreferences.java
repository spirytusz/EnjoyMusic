package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.global.MainApplication;

public class MusicSharedPreferences {

    private static final String TAG = "MusicSharedPreferences";

    private static final String CURRENT_PLAYING_MUSIC_KEY = "currentPlayingMusic Key";
    private static final String PLAY_MODE_KEY = "playModeKey";

    private static final String DEFAULT_MUSIC = "default result";
    private static final int DEFAULT_PLAY_MODE = -1;

    private MusicSharedPreferences() {
    }

    // save Music to storage will always in background when background be destroy
    public static void saveMusic(Music music) {
        if (music != null) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = MainApplication.getBackgroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
            String json = gson.toJson(music);
            editor.putString(CURRENT_PLAYING_MUSIC_KEY, json);
            editor.apply();
        }
    }

    // restore music will in both foreground and background when init
    public static Music restoreMusic(Context context) {
        Gson gson = new Gson();
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_KEY, DEFAULT_MUSIC);
        if (DEFAULT_MUSIC.equals(json)) {
            return null;
        } else {
            return gson.fromJson(json, Music.class);
        }
    }

    // restore music playMode will in both foreground and background when init
    public static int restorePlayMode(Context context) {
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        return pref.getInt(PLAY_MODE_KEY, DEFAULT_PLAY_MODE);
    }
}
