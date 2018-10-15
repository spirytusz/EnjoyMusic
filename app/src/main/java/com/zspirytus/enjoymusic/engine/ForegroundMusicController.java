package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;

import org.simple.eventbus.EventBus;

/** 前台音乐播放控制器
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController {

    private static class SingletonHolder {
        private static ForegroundMusicController INSTANCE = new ForegroundMusicController();
    }

    private ForegroundMusicController() {
    }

    public static ForegroundMusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void play(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PLAY);
    }

    public void pause(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PAUSE);
    }

    public void seekTo(int msec) {
        EventBus.getDefault().post(msec, Constant.EventBusTag.SEEK_TO);
    }

    public void release() {
        SingletonHolder.INSTANCE = null;
    }
}
