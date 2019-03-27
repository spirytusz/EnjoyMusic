package com.zspirytus.enjoymusic.listeners;

import com.zspirytus.enjoymusic.db.table.Music;

public interface IOnRemotePlayListener {
    void onPlay(Music music);
}
