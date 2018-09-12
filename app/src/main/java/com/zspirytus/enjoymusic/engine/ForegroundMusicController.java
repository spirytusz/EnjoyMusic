package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController {

    private static ForegroundMusicController INSTANCE;

    private ForegroundMusicController() {

    }

    public static ForegroundMusicController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ForegroundMusicController();
        }
        return INSTANCE;
    }

    public void play(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PLAY);
    }

    public void pause(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PAUSE);
    }

    public void stop(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.STOP);
    }

    public void seekTo(int msec) {
        EventBus.getDefault().post(msec, Constant.EventBusTag.SEEK_TO);
    }

    public void release() {
        INSTANCE = null;
    }
}
