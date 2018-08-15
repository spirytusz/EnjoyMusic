package com.zspirytus.enjoymusic.entity;

import java.io.Serializable;

/**
 * Music实体类
 * Created by ZSpirytus on 2018/8/4.
 */

public class Music implements Serializable {

    private String path;
    private String mMusicName;
    private String mMusicThumbAlbumUri;
    private String mMusicArtist;
    private long duration;
    private String size;

    public Music(String path, String mMusicName, String mMusicArtist, String mMusicThumbAlbumUri, long duration, String size) {
        this.path = path;
        this.mMusicName = mMusicName;
        this.mMusicThumbAlbumUri = mMusicThumbAlbumUri;
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

    public String getmMusicArtist() {
        return mMusicArtist;
    }

    public String getSize() {
        return size;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(path!= null){
            stringBuilder.append("path:"+path+", ");
        } else {
            stringBuilder.append("path:null"+", ");
        }
        if(mMusicName!= null){
            stringBuilder.append("mMusicName:"+mMusicName+", ");
        } else {
            stringBuilder.append("mMusicName:null"+", ");
        }
        if(mMusicThumbAlbumUri!= null){
            stringBuilder.append("mMusicThumbAlbumUri:"+mMusicThumbAlbumUri+", ");
        } else {
            stringBuilder.append("mMusicThumbAlbumUri:null"+", ");
        }
        if(mMusicArtist!= null){
            stringBuilder.append("mMusicArtist:"+mMusicArtist+", ");
        } else {
            stringBuilder.append("mMusicArtist:null"+", ");
        }
        if (duration != 0) {
            stringBuilder.append("duration:"+duration+", ");
        } else {
            stringBuilder.append("duration:0" + ", ");
        }
        if(size!= null){
            stringBuilder.append("size:"+size+" ");
        } else {
            stringBuilder.append("size:null ");
        }
        return "{"+stringBuilder.toString()+"}";
    }
}
