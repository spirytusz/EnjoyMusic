package com.zspirytus.enjoymusic.entity.table;

import com.zspirytus.enjoymusic.entity.Music;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class SongListItem extends LitePalSupport {

    @Column(unique = true)
    private String songListName;
    @Column
    private int musicCount;
    @Column
    private List<Music> musicList;

    public String getSongListName() {
        return songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }
}
