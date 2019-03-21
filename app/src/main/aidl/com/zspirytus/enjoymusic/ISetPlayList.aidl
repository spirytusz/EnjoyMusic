// ISetPlayList.aidl
package com.zspirytus.enjoymusic;

import java.util.List;

import com.zspirytus.enjoymusic.db.table.Music;

interface ISetPlayList {
    void setPlayList(in List<Music> playList);
    void appendMusicList(in List<Music> musicList);
    Music setPlayListAndGetFirstMusic(in List<Music> playList);
}
