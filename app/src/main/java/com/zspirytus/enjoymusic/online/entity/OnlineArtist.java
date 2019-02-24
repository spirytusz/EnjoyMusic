
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OnlineArtist {

    @SerializedName("albumSize")
    private Long mAlbumSize;
    @SerializedName("alia")
    private List<String> mAlia;
    @SerializedName("alias")
    private List<String> mAlias;
    @SerializedName("followed")
    private Boolean mFollowed;
    @SerializedName("id")
    private Long mId;
    @SerializedName("img1v1")
    private Long mImg1v1;
    @SerializedName("img1v1Url")
    private String mImg1v1Url;
    @SerializedName("mvSize")
    private Long mMvSize;
    @SerializedName("name")
    private String mName;
    @SerializedName("picId")
    private Long mPicId;
    @SerializedName("picUrl")
    private String mPicUrl;
    @SerializedName("trans")
    private String mTrans;
    @SerializedName("transNames")
    private List<String> mTransNames;

    public Long getAlbumSize() {
        return mAlbumSize;
    }

    public void setAlbumSize(Long albumSize) {
        mAlbumSize = albumSize;
    }

    public List<String> getAlia() {
        return mAlia;
    }

    public void setAlia(List<String> alia) {
        mAlia = alia;
    }

    public List<String> getAlias() {
        return mAlias;
    }

    public void setAlias(List<String> alias) {
        mAlias = alias;
    }

    public Boolean getFollowed() {
        return mFollowed;
    }

    public void setFollowed(Boolean followed) {
        mFollowed = followed;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getImg1v1() {
        return mImg1v1;
    }

    public void setImg1v1(Long img1v1) {
        mImg1v1 = img1v1;
    }

    public String getImg1v1Url() {
        return mImg1v1Url;
    }

    public void setImg1v1Url(String img1v1Url) {
        mImg1v1Url = img1v1Url;
    }

    public Long getMvSize() {
        return mMvSize;
    }

    public void setMvSize(Long mvSize) {
        mMvSize = mvSize;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Long getPicId() {
        return mPicId;
    }

    public void setPicId(Long picId) {
        mPicId = picId;
    }

    public String getPicUrl() {
        return mPicUrl;
    }

    public void setPicUrl(String picUrl) {
        mPicUrl = picUrl;
    }

    public String getTrans() {
        return mTrans;
    }

    public void setTrans(String trans) {
        mTrans = trans;
    }

    public List<String> getTransNames() {
        return mTransNames;
    }

    public void setTransNames(List<String> transNames) {
        mTransNames = transNames;
    }

}
