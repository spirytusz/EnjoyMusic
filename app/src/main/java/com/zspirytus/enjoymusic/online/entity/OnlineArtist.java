
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class OnlineArtist {

    @Expose
    private Long albumSize;
    @Expose
    private List<String> alia;
    @Expose
    private List<String> alias;
    @Expose
    private Boolean followed;
    @Expose
    private Long id;
    @Expose
    private Long img1v1;
    @Expose
    private String img1v1Url;
    @Expose
    private Long mvSize;
    @Expose
    private String name;
    @Expose
    private Long picId;
    @Expose
    private String picUrl;
    @Expose
    private String trans;
    @Expose
    private List<String> transNames;

    public Long getAlbumSize() {
        return albumSize;
    }

    public void setAlbumSize(Long albumSize) {
        this.albumSize = albumSize;
    }

    public List<String> getAlia() {
        return alia;
    }

    public void setAlia(List<String> alia) {
        this.alia = alia;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public Boolean getFollowed() {
        return followed;
    }

    public void setFollowed(Boolean followed) {
        this.followed = followed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getImg1v1() {
        return img1v1;
    }

    public void setImg1v1(Long img1v1) {
        this.img1v1 = img1v1;
    }

    public String getImg1v1Url() {
        return img1v1Url;
    }

    public void setImg1v1Url(String img1v1Url) {
        this.img1v1Url = img1v1Url;
    }

    public Long getMvSize() {
        return mvSize;
    }

    public void setMvSize(Long mvSize) {
        this.mvSize = mvSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

    public List<String> getTransNames() {
        return transNames;
    }

    public void setTransNames(List<String> transNames) {
        this.transNames = transNames;
    }

}
