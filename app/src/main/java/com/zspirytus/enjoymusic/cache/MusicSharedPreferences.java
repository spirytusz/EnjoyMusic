package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MusicSharedPreferences {

    private static final String TAG = "MusicSharedPreferences";

    private static final String CURRENT_PLAYING_MUSIC_KEY = "currentPlayingMusic Key";
    private static final String PLAY_MODE_KEY = "playModeKey";
    private static final String PLAY_LIST_KEY = "playListKey";
    private static final String PLAY_HISTORY_KEY = "playHistoryKey";

    private static final String DEFAULT_MUSIC = "default result";
    private static final int DEFAULT_PLAY_MODE = 0;

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

    // save playMode
    public static void savePlayMode(int playMode) {
        SharedPreferences.Editor editor = MainApplication.getBackgroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putInt(PLAY_MODE_KEY, playMode);
        editor.apply();
    }

    // save PlayList
    public static void savePlayList(List<Music> playList) {
        Gson gson = new Gson();
        String json = gson.toJson(playList);
        SharedPreferences.Editor editor = MainApplication.getBackgroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putString(PLAY_LIST_KEY, json);
        editor.apply();
    }

    // restore PlayList
    public static List<Music> restorePlayList(Context context) {
        Gson gson = new Gson();
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String json = pref.getString(PLAY_LIST_KEY, "");
        if (json.length() > 0) {
            return gson.fromJson(json, new TypeToken<List<Music>>() {
            }.getType());
        } else {
            return new ArrayList<>();
        }
    }

    // save PlayHistory
    public static void savePlayHistory(List<Music> playHistory) {
        Gson gson = new Gson();
        LogUtil.e(MusicSharedPreferences.class.getSimpleName(), "savedPlayHistorySize = " + playHistory.size());
        String json = gson.toJson(playHistory);
        SharedPreferences.Editor editor = MainApplication.getBackgroundContext().getSharedPreferences(TAG, Context.MODE_PRIVATE).edit();
        editor.putString(PLAY_HISTORY_KEY, json);
        editor.apply();
    }

    // restore PlayHistory
    public static List<Music> restorePlayHistory(Context context) {
        Gson gson = new Gson();
        SharedPreferences pref = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        String json = pref.getString(PLAY_HISTORY_KEY, "");
        if (json.length() > 0) {
            return gson.fromJson(json, new TypeToken<List<Music>>() {
            }.getType());
        } else {
            return new ArrayList<>();
        }
    }
}
