package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PlayHistory {

    @Id
    private Long musicId;
    private Long timestamp;

    @Generated(hash = 964369655)
    public PlayHistory(Long musicId, Long timestamp) {
        this.musicId = musicId;
        this.timestamp = timestamp;
    }
    @Generated(hash = 2145518983)
    public PlayHistory() {
    }

    public Long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
