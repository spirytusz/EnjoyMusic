package com.zspirytus.enjoymusic.db.table;

import com.zspirytus.enjoymusic.entity.Music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Song {

    @Id
    private long songId;
    private long albumId;
    private long artistId;
    private String musicFilePath;
    private String musicName;
    private String musicAlbumName;
    private String musicThumbAlbumCoverPath;
    private String musicArtist;
    private long musicDuration;
    private String musicFileSize;
    private long musicAddDate;

    @Generated(hash = 1287573145)
    public Song(long songId, long albumId, long artistId, String musicFilePath,
                String musicName, String musicAlbumName,
                String musicThumbAlbumCoverPath, String musicArtist, long musicDuration,
                String musicFileSize, long musicAddDate) {
        this.songId = songId;
        this.albumId = albumId;
        this.artistId = artistId;
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
        return new Song(
                music.getId(),
                music.getAlbumId(),
                music.getArtistId(),
                music.getMusicFilePath(),
                music.getMusicName(),
                music.getMusicAlbumName(),
                music.getMusicThumbAlbumCoverPath(),
                music.getMusicArtist(),
                music.getMusicDuration(),
                music.getMusicFileSize(),
                music.getMusicAddDate()
        );
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

    public long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
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
