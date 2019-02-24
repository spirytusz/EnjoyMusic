package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.db.table.Music;

import java.util.List;

public interface PlayListChangeDirectlyObserver {
    void onPlayListChangeDirectly(List<Music> playList);
}
