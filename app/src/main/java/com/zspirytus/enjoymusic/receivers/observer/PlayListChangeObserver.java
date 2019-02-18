package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.entity.MusicFilter;

public interface PlayListChangeObserver {

    void onPlayListChanged(MusicFilter filter);
}
