package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinArtistToSong {
    private long songId;
    private long artistId;

    @Generated(hash = 460116332)
    public JoinArtistToSong(long songId, long artistId) {
        this.songId = songId;
        this.artistId = artistId;
    }

    @Generated(hash = 29397823)
    public JoinArtistToSong() {
    }

    public long getSongId() {
        return this.songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }
}
