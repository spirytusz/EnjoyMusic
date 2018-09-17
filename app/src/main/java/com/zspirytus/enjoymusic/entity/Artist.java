package com.zspirytus.enjoymusic.entity;

/**
 * Created by ZSpirytus on 2018/9/13.
 */

public class Artist {

    private String artistName;
    private int musicCount = 1;

    public Artist(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getMusicCount() {
        return musicCount;
    }

    public void increaseMusicCount() {
        musicCount++;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Artist && artistName.equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return artistName.hashCode();
    }

    @Override
    public String toString() {
        return artistName;
    }
}
