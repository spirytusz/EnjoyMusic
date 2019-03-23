package com.zspirytus.enjoymusic.listeners;

import com.zspirytus.enjoymusic.db.table.Music;

public interface OnRemotePlayListener {
    void onPlay(Music music);
}
