package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinMusicToArtist {

    private long musicId;
    private long artistId;

    @Generated(hash = 1738214984)
    public JoinMusicToArtist(long musicId, long artistId) {
        this.musicId = musicId;
        this.artistId = artistId;
    }

    @Generated(hash = 71194154)
    public JoinMusicToArtist() {
    }

    public long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

}
