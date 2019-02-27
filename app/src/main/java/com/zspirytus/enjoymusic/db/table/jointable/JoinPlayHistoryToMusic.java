package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinPlayHistoryToMusic {

    private Long playHistoryId;
    @Id
    private Long musicId;

    private Long timeStamp;

    @Generated(hash = 1113373744)
    public JoinPlayHistoryToMusic(Long playHistoryId, Long musicId,
                                  Long timeStamp) {
        this.playHistoryId = playHistoryId;
        this.musicId = musicId;
        this.timeStamp = timeStamp;
    }

    @Generated(hash = 205670095)
    public JoinPlayHistoryToMusic() {
    }

    public Long getPlayHistoryId() {
        return this.playHistoryId;
    }

    public void setPlayHistoryId(Long playHistoryId) {
        this.playHistoryId = playHistoryId;
    }

    public Long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public Long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

}
