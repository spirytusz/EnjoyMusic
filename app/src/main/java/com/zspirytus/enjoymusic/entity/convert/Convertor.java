package com.zspirytus.enjoymusic.entity.convert;

import com.zspirytus.enjoymusic.db.table.JoinAlbumToSong;
import com.zspirytus.enjoymusic.db.table.JoinArtistToSong;
import com.zspirytus.enjoymusic.db.table.Song;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

public class Convertor {

    private Convertor() {
        throw new AssertionError();
    }

    public static Music createMusic(Song song) {
        return new Music(
                song.getSongId(),
                song.getAlbumId(),
                song.getArtistId(),
                song.getMusicFilePath(),
                song.getMusicName(),
                song.getMusicArtist(),
                song.getMusicAlbumName(),
                song.getMusicThumbAlbumCoverPath(),
                song.getMusicDuration(),
                song.getMusicFileSize(),
                song.getMusicAddDate()
        );
    }

    public static List<Music> createMusicList(List<Song> songs) {
        List<Music> musicList = new ArrayList<>();
        for (Song song : songs) {
            musicList.add(createMusic(song));
        }
        return musicList;
    }

    public static Song createSong(Music music) {
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

    public static List<Song> createSongs(List<Music> musicList) {
        List<Song> songs = new ArrayList<>();
        for (Music music : musicList) {
            songs.add(createSong(music));
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
}
