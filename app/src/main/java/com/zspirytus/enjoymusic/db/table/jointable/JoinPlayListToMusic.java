package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinPlayListToMusic {

    private Long playListId;
    private Long musicId;

    @Generated(hash = 1485381102)
    public JoinPlayListToMusic(Long playListId, Long musicId) {
        this.playListId = playListId;
        this.musicId = musicId;
    }

    @Generated(hash = 55597161)
    public JoinPlayListToMusic() {
    }

    public Long getPlayListId() {
        return this.playListId;
    }

    public void setPlayListId(Long playListId) {
        this.playListId = playListId;
    }

    public Long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
}
