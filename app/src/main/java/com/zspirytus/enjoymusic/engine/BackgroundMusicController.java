package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.entity.Music;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/9.
 */

public class BackgroundMusicController {

    private static BackgroundMusicController INSTANCE;

    private BackgroundMusicController() {
    }

    public static BackgroundMusicController getInstance() {
        if (INSTANCE == null)
            INSTANCE = new BackgroundMusicController();
        return INSTANCE;
    }

    public void play(Music music) {
        EventBus.getDefault().post(music, FinalValue.EventBusTag.PLAY);
    }

    public void pause(Music music) {
        EventBus.getDefault().post(music, FinalValue.EventBusTag.PAUSE);
    }

    public void stop(Music music) {
        EventBus.getDefault().post(music, FinalValue.EventBusTag.STOP);
    }

    public void release() {
        INSTANCE = null;
    }
}
