package com.zspirytus.enjoymusic.interfaces;

import com.zspirytus.enjoymusic.entity.Music;

public interface RemotePlayMusicChangeCallback {

    void onPlayMusicChange(Music currentPlayingMusic);
}
