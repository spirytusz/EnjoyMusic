package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PlayList {

    @Id
    private Long musicId;

    @Generated(hash = 1425714925)
    public PlayList(Long musicId) {
        this.musicId = musicId;
    }

    @Generated(hash = 438209239)
    public PlayList() {
    }

    public Long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
}
