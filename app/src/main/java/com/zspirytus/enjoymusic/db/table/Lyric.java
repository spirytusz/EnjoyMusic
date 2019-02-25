package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Lyric {

    @Id
    private Long musicId;
    private String lyricFilePath;

    @Generated(hash = 1417828378)
    public Lyric(Long musicId, String lyricFilePath) {
        this.musicId = musicId;
        this.lyricFilePath = lyricFilePath;
    }

    @Generated(hash = 2083827090)
    public Lyric() {
    }

    public Long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public String getLyricFilePath() {
        return this.lyricFilePath;
    }

    public void setLyricFilePath(String lyricFilePath) {
        this.lyricFilePath = lyricFilePath;
    }
}
