package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.db.table.Music;

import java.util.List;

public interface PlayHistoryObserver {
    void onPlayHistoryChange(List<Music> playHistory);
}
