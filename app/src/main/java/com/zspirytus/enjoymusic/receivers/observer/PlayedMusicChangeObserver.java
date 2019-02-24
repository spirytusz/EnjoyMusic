package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.db.table.Music;

/**
 * 播放音乐改变观察接口
 * Created by ZSpirytus on 2018/9/9.
 */

public interface PlayedMusicChangeObserver {

    void onPlayedMusicChanged(Music music);
}
