// IPlayedMusicChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import com.zspirytus.enjoymusic.db.table.Music;

interface IPlayedMusicChangeObserver {
    void onPlayMusicChange(in Music currentPlayingMusic);
}
