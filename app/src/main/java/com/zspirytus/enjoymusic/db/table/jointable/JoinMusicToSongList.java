package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinMusicToSongList {


    private long musicId;
    private long songListId;

    @Generated(hash = 206329586)
    public JoinMusicToSongList(long musicId, long songListId) {
        this.musicId = musicId;
        this.songListId = songListId;
    }

    @Generated(hash = 694273801)
    public JoinMusicToSongList() {
    }

    public long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getSongListId() {
        return this.songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }
}
