package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.db.table.Music;

public interface MusicDeleteObserver {
    void onMusicDelete(Music music);
}
