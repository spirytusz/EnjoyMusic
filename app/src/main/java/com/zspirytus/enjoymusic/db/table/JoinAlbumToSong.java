package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinAlbumToSong {
    private long songId;
    private long albumId;

    @Generated(hash = 1187071164)
    public JoinAlbumToSong(long songId, long albumId) {
        this.songId = songId;
        this.albumId = albumId;
    }

    @Generated(hash = 13139241)
    public JoinAlbumToSong() {
    }

    public long getSongId() {
        return this.songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
