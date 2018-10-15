package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;

import org.simple.eventbus.EventBus;

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
        EventBus.getDefault().post(music, Constant.EventBusTag.PLAY);
    }

    public void pause(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PAUSE);
    }

    public void stop(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.STOP);
    }
}
