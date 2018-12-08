package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.entity.Music;

public class MusicSharedPreferences {

    public static final String CURRENT_PLAYING_MUSIC_KEY = "currentPlayingMusic Key";

    private static final String TAG = "MusicSharedPreferences";

    private static final String DEFAULT_RESULT = "default result";

    private MusicSharedPreferences() {
    }

    public static void save(Music music, String key) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = MyApplication.getBackgroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        String json = gson.toJson(music);
        editor.putString(key, json);
        editor.commit();
    }

    public static Music restore(String key) {
        Gson gson = new Gson();
        SharedPreferences pref = MyApplication.getBackgroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String json = pref.getString(key, DEFAULT_RESULT);
        if (DEFAULT_RESULT.equals(json)) {
            return new Music("", "", "", "", "", 0, "");
        } else {
            Music music = gson.fromJson(json, Music.class);
            return music;
        }
    }
}
