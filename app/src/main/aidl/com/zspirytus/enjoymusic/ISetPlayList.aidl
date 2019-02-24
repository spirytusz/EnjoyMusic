// ISetPlayList.aidl
package com.zspirytus.enjoymusic;

import java.util.List;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;

interface ISetPlayList {
    void setPlayList(in MusicFilter musicFilter);
    void setPlayListDirectly(in List<Music> playList);
    void appendMusicListDirectly(in List<Music> musicList);
    void appendMusic(in MusicFilter musicFilter);
}
