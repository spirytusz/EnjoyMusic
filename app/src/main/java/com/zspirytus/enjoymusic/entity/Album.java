package com.zspirytus.enjoymusic.entity;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class Album {

    private String albumName;
    private String artist;
    private String albumCoverPath;

    public Album(String albumName, String albumCoverPath, String artist) {
        this.albumName = albumName;
        this.albumCoverPath = albumCoverPath;
        this.artist = artist;
    }

    public String getAlbumCoverPath() {
        return albumCoverPath;
    }

    public void setAlbumCoverPath(String albumCoverPath) {
        this.albumCoverPath = albumCoverPath;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("albumName = ");
        stringBuilder.append(albumName != null ? albumName + ", " : "null, ");
        stringBuilder.append("albumCoverPath = ");
        stringBuilder.append(albumCoverPath != null ? albumCoverPath + ", " : "null, ");
        stringBuilder.append("artist = ");
        stringBuilder.append(artist != null ? artist : "null");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Album && this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
