package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinArtistToAlbum {
    private long albumId;
    private long artistId;

    @Generated(hash = 859570351)
    public JoinArtistToAlbum(long albumId, long artistId) {
        this.albumId = albumId;
        this.artistId = artistId;
    }

    @Generated(hash = 906843595)
    public JoinArtistToAlbum() {
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JoinArtistToAlbum && obj.hashCode() == hashCode();
    }

    @Override
    public String toString() {
        return "{albumId = " + albumId + ", artistId = " + artistId + "}";
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }
}
