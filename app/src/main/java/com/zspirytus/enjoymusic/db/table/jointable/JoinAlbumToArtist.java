package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class JoinAlbumToArtist {

    private long albumId;
    private long artistId;

    @Generated(hash = 1849266952)
    public JoinAlbumToArtist(long albumId, long artistId) {
        this.albumId = albumId;
        this.artistId = artistId;
    }

    @Generated(hash = 1738802940)
    public JoinAlbumToArtist() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinAlbumToArtist that = (JoinAlbumToArtist) o;
        return albumId == that.albumId &&
                artistId == that.artistId;
    }

    @Override
    public String toString() {
        return "JoinAlbumToArtist{" +
                "albumId=" + albumId +
                ", artistId=" + artistId +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumId, artistId);
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
