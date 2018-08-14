package com.zspirytus.enjoymusic.receivers;

/**
 * 音乐播放状态观察者接口
 * Created by ZSpirytus on 2018/8/10.
 */

public interface MusicPlayStateObserver {

    void onPlayingState(boolean isPlaying);

    void onPlayCompleted();

}
