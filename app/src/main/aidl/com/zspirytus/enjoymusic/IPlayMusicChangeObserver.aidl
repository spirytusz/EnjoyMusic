// IPlayMusicChangeObserver.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.entity.Music;

interface IPlayMusicChangeObserver {
    void onMusicChange(in Music music);
}
