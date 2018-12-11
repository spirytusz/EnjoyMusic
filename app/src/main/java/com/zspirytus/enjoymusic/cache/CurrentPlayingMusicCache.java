package com.zspirytus.enjoymusic.cache;

import com.zspirytus.enjoymusic.entity.Music;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class CurrentPlayingMusicCache {

    private static final CurrentPlayingMusicCache ourInstance = new CurrentPlayingMusicCache();

    private Music currentPlayingMusic;

    public static CurrentPlayingMusicCache getInstance() {
        return ourInstance;
    }

    private CurrentPlayingMusicCache() {
        currentPlayingMusic = MusicSharedPreferences.restoreMusic(MyApplication.getBackgroundContext());
    }

    public void setCurrentPlayingMusic(Music music) {
        currentPlayingMusic = music;
    }

    public Music getCurrentPlayingMusic() {
        return currentPlayingMusic;
    }

}
