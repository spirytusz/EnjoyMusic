package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class JoinMusicToAlbum {

    private long musicId;
    private long albumId;

    @Generated(hash = 1423397728)
    public JoinMusicToAlbum(long musicId, long albumId) {
        this.musicId = musicId;
        this.albumId = albumId;
    }

    @Generated(hash = 1000182565)
    public JoinMusicToAlbum() {
    }

    @Override
    public String toString() {
        return "JoinMusicToAlbum{" +
                "musicId=" + musicId +
                ", albumId=" + albumId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinMusicToAlbum that = (JoinMusicToAlbum) o;
        return musicId == that.musicId &&
                albumId == that.albumId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(musicId, albumId);
    }

    public long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
