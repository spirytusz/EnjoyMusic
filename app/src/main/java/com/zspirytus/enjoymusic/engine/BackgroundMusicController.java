package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.entity.Music;

/**
 * Created by ZSpirytus on 2018/9/9.
 */

public class BackgroundMusicController {

    private static class SingletonHolder {
        private static BackgroundMusicController INSTANCE = new BackgroundMusicController();
    }

    private BackgroundMusicController() {
    }

    public static BackgroundMusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void play(Music music) {
    }

    public void pause(Music music) {
    }
}
