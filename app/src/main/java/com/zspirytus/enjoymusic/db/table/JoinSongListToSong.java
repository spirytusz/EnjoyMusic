package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinSongListToSong {

    private long songId;
    private long songListId;

    @Generated(hash = 491284081)
    public JoinSongListToSong(long songId, long songListId) {
        this.songId = songId;
        this.songListId = songListId;
    }

    @Generated(hash = 937056404)
    public JoinSongListToSong() {
    }

    public long getSongId() {
        return this.songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getSongListId() {
        return this.songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }

}