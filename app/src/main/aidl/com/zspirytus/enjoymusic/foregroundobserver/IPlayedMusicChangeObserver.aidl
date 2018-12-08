// IPlayedMusicChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import com.zspirytus.enjoymusic.entity.Music;

interface IPlayedMusicChangeObserver {
    void onPlayMusicChange(in Music currentPlayingMusic);
}
