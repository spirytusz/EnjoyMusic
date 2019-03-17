// IMusicDeleteObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import com.zspirytus.enjoymusic.db.table.Music;

interface IMusicDeleteObserver {
    void onMusicDelete(in Music music);
}
