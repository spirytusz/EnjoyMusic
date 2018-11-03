// IPlayMusicChangeObserver.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.entity.Music;

// Declare any non-default types here with import statements

interface IPlayMusicChangeObserver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onPlayedMusicChanged(in Music music);
}
