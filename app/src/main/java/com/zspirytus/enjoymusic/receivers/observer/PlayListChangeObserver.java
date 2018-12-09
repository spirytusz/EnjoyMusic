package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.entity.Music;

import java.util.List;

public interface PlayListChangeObserver {

    void onPlayListChange(List<Music> playList);
}
