package com.zspirytus.enjoymusic.entity;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;

public class MusicMetaDataListItem {

    private boolean isArtistArt = false;
    private Artist artist;

    private boolean isMusic = false;
    private Music music;

    private boolean isTitle;
    private String title;

    private boolean isDownloadAlbumArtView = false;

    private boolean isSingleEditText = false;
    private String editTextTitle;
    private String editTextDefaultText;

    private boolean isDuplicateEditText = false;
    private String firstEditTextTitle;
    private String firstEditTextDefaultText;
    private String secondEditTextTitle;
    private String secondEditTextDefaultText;

    private Album album;

    /*@Override
    public String toString() {
        if(isArtistArt) {
            return artist.toString();
        } else if(isMusic) {
            return music.toString();
        } else if(isTitle) {
            return title;
        } else if(isDownloadAlbumArtView) {
            return "downloadView";
        } else if(isSingleEditText) {
            return editTextDefaultText;
        } else {
            return "none";
        }
    }*/

    public boolean isArtistArt() {
        return isArtistArt;
    }

    public void setArtistArt(boolean artistArt) {
        isArtistArt = artistArt;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
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

    public boolean isDownloadAlbumArtView() {
        return isDownloadAlbumArtView;
    }

    public void setDownloadAlbumArtView(boolean downloadAlbumArtView) {
        isDownloadAlbumArtView = downloadAlbumArtView;
    }

    public boolean isSingleEditText() {
        return isSingleEditText;
    }

    public void setSingleEditText(boolean singleEditText) {
        isSingleEditText = singleEditText;
    }

    public String getEditTextTitle() {
        return editTextTitle;
    }

    public void setEditTextTitle(String editTextTitle) {
        this.editTextTitle = editTextTitle;
    }

    public String getEditTextDefaultText() {
        return editTextDefaultText;
    }

    public void setEditTextDefaultText(String editTextDefaultText) {
        this.editTextDefaultText = editTextDefaultText;
    }

    public boolean isDuplicateEditText() {
        return isDuplicateEditText;
    }

    public void setDuplicateEditText(boolean duplicateEditText) {
        isDuplicateEditText = duplicateEditText;
    }

    public String getFirstEditTextTitle() {
        return firstEditTextTitle;
    }

    public void setFirstEditTextTitle(String firstEditTextTitle) {
        this.firstEditTextTitle = firstEditTextTitle;
    }

    public String getFirstEditTextDefaultText() {
        return firstEditTextDefaultText;
    }

    public void setFirstEditTextDefaultText(String firstEditTextDefaultText) {
        this.firstEditTextDefaultText = firstEditTextDefaultText;
    }

    public String getSecondEditTextTitle() {
        return secondEditTextTitle;
    }

    public void setSecondEditTextTitle(String secondEditTextTitle) {
        this.secondEditTextTitle = secondEditTextTitle;
    }

    public String getSecondEditTextDefaultText() {
        return secondEditTextDefaultText;
    }

    public void setSecondEditTextDefaultText(String secondEditTextDefaultText) {
        this.secondEditTextDefaultText = secondEditTextDefaultText;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
