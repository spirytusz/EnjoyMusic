package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.entity.MusicFilter;

public interface PlayListChangeObserver {

    void onPlayListChange(MusicFilter filter);
}
