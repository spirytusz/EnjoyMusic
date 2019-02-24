// IPlayListChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import java.util.List;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;

interface IPlayListChangeObserver {
    void onPlayListChange(in MusicFilter filter);
    void onPlayListChangeDirectly(in List<Music> playList);
}
