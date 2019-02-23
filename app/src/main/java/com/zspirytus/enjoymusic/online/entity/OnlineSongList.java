
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OnlineSongList {

    @SerializedName("author")
    private String mAuthor;
    @SerializedName("desc")
    private String mDesc;
    @SerializedName("id")
    private String mId;
    @SerializedName("logo")
    private String mLogo;
    @SerializedName("songnum")
    private String mSongnum;
    @SerializedName("songs")
    private List<OnlineSong> mSongs;
    @SerializedName("title")
    private String mTitle;

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getDesc() {
        return mDesc;
    }

    @Override
    public String toString() {
        return "OnlineSongList{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mDesc='" + mDesc + '\'' +
                ", mId='" + mId + '\'' +
                ", mLogo='" + mLogo + '\'' +
                ", mSongnum='" + mSongnum + '\'' +
                ", mSongs=" + mSongs +
                ", mTitle='" + mTitle + '\'' +
                '}';
    }

    public void setDesc(String desc) {
        mDesc = desc;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String logo) {
        mLogo = logo;
    }

    public String getSongnum() {
        return mSongnum;
    }

    public void setSongnum(String songnum) {
        mSongnum = songnum;
    }

    public List<OnlineSong> getSongs() {
        return mSongs;
    }

    public void setSongs(List<OnlineSong> songs) {
        mSongs = songs;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
