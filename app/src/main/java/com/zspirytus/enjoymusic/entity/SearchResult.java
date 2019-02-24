package com.zspirytus.enjoymusic.entity;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;

public class SearchResult {

    private boolean isTitle = false;
    private String title;

    private boolean isMusic = false;
    private Music music;

    private boolean isAlbum = false;
    private Album album;

    private boolean isArtist = false;
    private Artist artist;

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isMusic() {
        return isMusic;
    }

    public void setMusic(boolean music) {
        isMusic = music;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public void setAlbum(boolean album) {
        isAlbum = album;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public void setArtist(boolean artist) {
        isArtist = artist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        if (isTitle) {
            return "Title = " + title;
        } else if (isMusic) {
            return "Music = " + music.getMusicName();
        } else if (isAlbum) {
            return "Album = " + album.getAlbumName();
        } else {
            return "OnlineArtist = " + artist.getArtistName();
        }
    }
}
