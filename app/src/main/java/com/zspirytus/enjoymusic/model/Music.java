package com.zspirytus.enjoymusic.model;

import java.io.Serializable;

/**
 * Created by ZSpirytus on 2018/8/4.
 */

public class Music implements Serializable {

    private String path;
    private String mMusicName;
    private String mMusicThumbAlbumUri;
    private String mMusicAlbumUri;
    private String mMusicArtist;
    private String duration;
    private String size;

    public Music(String path, String mMusicName, String mMusicArtist, String mMusicThumbAlbumUri, String mMusicAlbumUri, String duration, String size) {
        this.path = path;
        this.mMusicName = mMusicName;
        this.mMusicThumbAlbumUri = mMusicThumbAlbumUri;
        this.mMusicAlbumUri = mMusicAlbumUri;
        this.mMusicArtist = mMusicArtist;
        this.duration = duration;
        this.size = size;
    }

    public String getPath() {
        return path;
    }


    public String getmMusicName() {
        return mMusicName;
    }

    public String getmMusicThumbAlbumUri() {
        return mMusicThumbAlbumUri;
    }

    public String getmMusicAlbumUri() {
        return mMusicAlbumUri;
    }

    public String getmMusicArtist() {
        return mMusicArtist;
    }

    public String getSize() {
        return size;
    }

    public String getDuration() {
        return duration;
    }

}
