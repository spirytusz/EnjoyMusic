// IPlayListChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import java.util.List;

import com.zspirytus.enjoymusic.db.table.Music;

interface IPlayListChangeObserver {
    void onPlayListChange(in List<Music> playList);
}
