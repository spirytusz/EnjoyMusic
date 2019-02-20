package com.zspirytus.enjoymusic.db.table;

import com.zspirytus.enjoymusic.entity.Music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Song {

    @Id
    private long songId;
    private String musicFilePath;
    private String musicName;
    private String musicAlbumName;
    private String musicThumbAlbumCoverPath;
    private String musicArtist;
    private long musicDuration;
    private String musicFileSize;
    private long musicAddDate;

    @Generated(hash = 207801051)
    public Song(long songId, String musicFilePath, String musicName,
                String musicAlbumName, String musicThumbAlbumCoverPath,
                String musicArtist, long musicDuration, String musicFileSize,
                long musicAddDate) {
        this.songId = songId;
        this.musicFilePath = musicFilePath;
        this.musicName = musicName;
        this.musicAlbumName = musicAlbumName;
        this.musicThumbAlbumCoverPath = musicThumbAlbumCoverPath;
        this.musicArtist = musicArtist;
        this.musicDuration = musicDuration;
        this.musicFileSize = musicFileSize;
        this.musicAddDate = musicAddDate;
    }

    @Generated(hash = 87031450)
    public Song() {
    }

    public static Song create(Music music) {
        Song song = new Song();
        song.songId = music.getId();
        song.musicName = music.getMusicName();
        song.musicAlbumName = music.getMusicAlbumName();
        song.musicArtist = music.getMusicArtist();
        song.musicAddDate = music.getMusicAddDate();
        song.musicDuration = music.getMusicDuration();
        song.musicFilePath = music.getMusicFilePath();
        song.musicThumbAlbumCoverPath = music.getMusicThumbAlbumCoverPath();
        song.musicFileSize = music.getMusicFileSize();
        return song;
    }

    public Music create() {
        return new Music(
                songId,
                musicFilePath,
                musicName,
                musicArtist,
                musicAlbumName,
                musicThumbAlbumCoverPath,
                musicDuration,
                musicFileSize,
                musicAddDate
        );
    }

    public long getSongId() {
        return this.songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getMusicFilePath() {
        return this.musicFilePath;
    }

    public void setMusicFilePath(String musicFilePath) {
        this.musicFilePath = musicFilePath;
    }

    public String getMusicName() {
        return this.musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicAlbumName() {
        return this.musicAlbumName;
    }

    public void setMusicAlbumName(String musicAlbumName) {
        this.musicAlbumName = musicAlbumName;
    }

    public String getMusicThumbAlbumCoverPath() {
        return this.musicThumbAlbumCoverPath;
    }

    public void setMusicThumbAlbumCoverPath(String musicThumbAlbumCoverPath) {
        this.musicThumbAlbumCoverPath = musicThumbAlbumCoverPath;
    }

    public String getMusicArtist() {
        return this.musicArtist;
    }

    public void setMusicArtist(String musicArtist) {
        this.musicArtist = musicArtist;
    }

    public long getMusicDuration() {
        return this.musicDuration;
    }

    public void setMusicDuration(long musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getMusicFileSize() {
        return this.musicFileSize;
    }

    public void setMusicFileSize(String musicFileSize) {
        this.musicFileSize = musicFileSize;
    }

    public long getMusicAddDate() {
        return this.musicAddDate;
    }

    public void setMusicAddDate(long musicAddDate) {
        this.musicAddDate = musicAddDate;
    }

}
