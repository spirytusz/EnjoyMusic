package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.db.table.Music;

import java.util.List;

public interface PlayListChangeObserver {
    void onPlayListChangeDirectly(List<Music> playList);
}
