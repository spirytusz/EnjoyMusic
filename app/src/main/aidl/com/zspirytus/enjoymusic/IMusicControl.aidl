// IMusicControl.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.db.table.Music;

interface IMusicControl {
    void play(in Music music);
    void pause();
    void playNext(boolean fromUser);
    void playPrevious();
    void setPlayMode(int playMode);
}
