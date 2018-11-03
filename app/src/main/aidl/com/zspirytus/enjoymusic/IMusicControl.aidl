// IMusicControl.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.entity.Music;

// Declare any non-default types here with import statements

interface IMusicControl {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void play(in Music music);
    void pause();
}
