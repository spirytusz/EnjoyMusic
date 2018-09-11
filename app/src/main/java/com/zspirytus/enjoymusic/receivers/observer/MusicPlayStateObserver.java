package com.zspirytus.enjoymusic.receivers.observer;

/**
 * 音乐播放状态观察接口
 * Created by ZSpirytus on 2018/8/10.
 */

public interface MusicPlayStateObserver {

    void onPlayingStateChanged(boolean isPlaying);
}
