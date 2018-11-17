package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.adapter.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.engine.MusicPlayedHistoryProvider;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.io.File;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class CurrentPlayingMusicCache implements PlayedMusicChangeObserver {
    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";
    private static final CurrentPlayingMusicCache ourInstance = new CurrentPlayingMusicCache();

    private Music currentPlayingMusic;

    public static CurrentPlayingMusicCache getInstance() {
        return ourInstance;
    }

    private CurrentPlayingMusicCache() {
        IPlayMusicChangeObserverImpl.getInstance().register(this);
    }

    public void setCurrentPlayingMusic(Music music) {
        currentPlayingMusic = music;
        MusicPlayedHistoryProvider.getInstance().put(music);
    }

    public Music getCurrentPlayingMusic() {
        return currentPlayingMusic;
    }

    protected void restoreCurrentPlayingMusic() {
        SharedPreferences pref = MyApplication.getGlobalContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_STRING_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Music music = gson.fromJson(json, Music.class);
            if (music != null) {
                File file = new File(music.getMusicFilePath());
                if (file.exists()) {
                    currentPlayingMusic = music;
                }
            }
        } else {
            /*List<Music> allMusicList = AllMusicCache.getInstance().getAllMusicList();
            if (allMusicList != null && allMusicList.size() > 0)
                currentPlayingMusic = allMusicList.get(0);*/
        }
    }

    public void saveCurrentPlayingMusic() {
        if (currentPlayingMusic != null) {
            SharedPreferences.Editor editor = MyApplication.getGlobalContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(currentPlayingMusic, currentPlayingMusic.getClass());
            editor.putString(CURRENT_PLAYING_MUSIC_STRING_KEY, json);
            editor.apply();
        }
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        currentPlayingMusic = music;
    }
}
