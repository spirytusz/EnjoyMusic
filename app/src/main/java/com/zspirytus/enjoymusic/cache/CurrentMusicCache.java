package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.io.File;

/**
 * Created by ZSpirytus on 2018/8/12.
 */

public class CurrentMusicCache {

    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";

    public static Music restoreCurrentPlayingMusic() {
        SharedPreferences pref = BaseActivity.getContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_STRING_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Music music = gson.fromJson(json, Music.class);
            if (music != null) {
                File file = new File(music.getPath());
                if (file.exists()) {
                    return music;
                }
            }
        }
        return null;
    }

    public static void saveCurrentPlayingMusic(Music currentPlayingMusic) {
        if (currentPlayingMusic != null) {
            SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(currentPlayingMusic, currentPlayingMusic.getClass());
            editor.putString(CURRENT_PLAYING_MUSIC_STRING_KEY, json);
            editor.commit();
        }
    }
}
