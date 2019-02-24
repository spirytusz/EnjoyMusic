package com.zspirytus.enjoymusic.entity.convert;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Convertor {

    private Convertor() {
        throw new AssertionError();
    }

    public static Music createMusic(Song song) {
        return new Music(
                song.getSongId(),
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
                music.getArtistArt(),
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

    public static List<ArtistTable> createAristTables(List<Artist> artists) {
        List<ArtistTable> artistTables = new ArrayList<>();
        for (Artist artist : artists) {
            artistTables.add(createArtistTable(artist));
        }
        return artistTables;
    }

    public static ArtistTable createArtistTable(Artist artist) {
        return new ArtistTable(
                artist.get_id(),
                artist.getArtistName(),
                artist.getNumberOfAlbums(),
                artist.getNumberOfTracks()
        );
    }

    public static Artist createArtist(ArtistTable artistTable) {
        return new Artist(
                artistTable.getArtistId(),
                artistTable.getMArtistName(),
                artistTable.getMNumberOfAlbums(),
                artistTable.getMNumberOfTracks()
        );
    }

    public static List<AlbumTable> createAlbumTables(List<Album> albums) {
        List<AlbumTable> albumTables = new ArrayList<>();
        for (Album album : albums) {
            albumTables.add(createAlbumTable(album));
        }
        return albumTables;
    }

    public static Album createAlbum(AlbumTable albumTable) {
        return new Album(
                albumTable.getAlbumId(),
                albumTable.getMAlbumName(),
                albumTable.getMAlbumCoverPath(),
                albumTable.getMArtist(),
                albumTable.getMFirstYear(),
                albumTable.getMLastYear(),
                albumTable.getMAlbumSongCount()
        );
    }

    public static List<Album> createAlbums(List<AlbumTable> albumTables) {
        List<Album> albums = new ArrayList<>();
        for (AlbumTable albumTable : albumTables) {
            albums.add(createAlbum(albumTable));
        }
        return albums;
    }

    public static AlbumTable createAlbumTable(Album album) {
        return new AlbumTable(
                album.get_id(),
                album.getAlbumName(),
                album.getAlbumCoverPath(),
                album.getArtist(),
                album.getFirstYear(),
                album.getLastYear(),
                album.getAlbumSongCount()
        );
    }

    public static List<JoinArtistToAlbum> createJoinArtistToAlbums(List<Music> musicList) {
        Set<JoinArtistToAlbum> joinArtistToAlbums = new HashSet<>();
        for (Music music : musicList) {
            JoinArtistToAlbum joinArtistToAlbum = new JoinArtistToAlbum();
            joinArtistToAlbum.setAlbumId(music.getAlbumId());
            joinArtistToAlbum.setArtistId(music.getArtistId());
            joinArtistToAlbums.add(joinArtistToAlbum);
        }
        return new ArrayList<>(joinArtistToAlbums);
    }
}
