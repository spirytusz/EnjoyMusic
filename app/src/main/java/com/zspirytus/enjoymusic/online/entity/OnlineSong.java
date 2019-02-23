
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OnlineSong {

    @SerializedName("id")
    private String mId;
    @SerializedName("lrc")
    private String mLrc;
    @SerializedName("name")
    private String mName;
    @SerializedName("pic")
    private String mPic;
    @SerializedName("singer")
    private String mSinger;
    @SerializedName("time")
    private Long mTime;
    @SerializedName("url")
    private String mUrl;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLrc() {
        return mLrc;
    }

    public void setLrc(String lrc) {
        mLrc = lrc;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPic() {
        return mPic;
    }

    public void setPic(String pic) {
        mPic = pic;
    }

    public String getSinger() {
        return mSinger;
    }

    public void setSinger(String singer) {
        mSinger = singer;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long time) {
        mTime = time;
    }

    @Override
    public String toString() {
        return "OnlineSong{" +
                "mId='" + mId + '\'' +
                ", mLrc='" + mLrc + '\'' +
                ", mName='" + mName + '\'' +
                ", mPic='" + mPic + '\'' +
                ", mSinger='" + mSinger + '\'' +
                ", mTime=" + mTime +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
