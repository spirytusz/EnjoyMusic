package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class JoinMusicToSongList {
    @Override
    public String toString() {
        return "JoinMusicToSongList{" +
                "musicId=" + musicId +
                ", songListId=" + songListId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinMusicToSongList that = (JoinMusicToSongList) o;
        return musicId == that.musicId &&
                songListId == that.songListId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(musicId, songListId);
    }

    public long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }

    public long getSongListId() {
        return this.songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }

    private long musicId;
    private long songListId;

    @Generated(hash = 206329586)
    public JoinMusicToSongList(long musicId, long songListId) {
        this.musicId = musicId;
        this.songListId = songListId;
    }

    @Generated(hash = 694273801)
    public JoinMusicToSongList() {
    }
}
