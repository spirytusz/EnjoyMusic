package com.zspirytus.enjoymusic.db.table;

import com.zspirytus.enjoymusic.entity.Music;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.ArrayList;
import java.util.List;

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

    public static List<Song> create(List<Music> musicList) {
        List<Song> songs = new ArrayList<>();
        for (Music music : musicList) {
            songs.add(Song.create(music));
        }
        return songs;
    }

    public static List<JoinAlbumToSong> createJoinAlbumToSongs(List<Music> musicList) {
        List<JoinAlbumToSong> joinAlbumToSongs = new ArrayList<>();
        for (Music music : musicList) {
            JoinAlbumToSong joinAlbumToSong = new JoinAlbumToSong();
            joinAlbumToSong.setSongId(music.getId());
            joinAlbumToSong.setAlbumId(music.getAlbumId());
            joinAlbumToSongs.add(joinAlbumToSong);
        }
        return joinAlbumToSongs;
    }

    public static List<JoinArtistToSong> createJoinArtistToSongs(List<Music> musicList) {
        List<JoinArtistToSong> joinArtistToSongs = new ArrayList<>();
        for (Music music : musicList) {
            JoinArtistToSong joinArtistToSong = new JoinArtistToSong();
            joinArtistToSong.setSongId(music.getId());
            joinArtistToSong.setArtistId(music.getArtistId());
            joinArtistToSongs.add(joinArtistToSong);
        }
        return joinArtistToSongs;
    }

    public Music create() {
        return new Music(
                songId,
                albumId,
                artistId,
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
