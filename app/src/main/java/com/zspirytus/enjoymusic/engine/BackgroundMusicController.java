package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

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
        if (music != null) {
            MediaPlayController.getInstance().play(music);
        }
    }

    public void pause() {
        MediaPlayController.getInstance().pause();
    }

    public boolean isPlaying() {
        return MediaPlayController.getInstance().isPlaying();
    }

    public void release() {
        MediaPlayController.getInstance().release();
    }
}
