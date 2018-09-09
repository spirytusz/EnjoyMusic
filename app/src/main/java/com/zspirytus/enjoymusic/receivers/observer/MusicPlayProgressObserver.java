package com.zspirytus.enjoymusic.receivers.observer;

/**
 * 音乐播放进度观察接口
 * Created by ZSpirytus on 2018/8/17.
 */

public interface MusicPlayProgressObserver {

    void onProgressChange(int progress);
}
