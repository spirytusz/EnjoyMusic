
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class LiteOnlineSongListItem {

    @Expose
    private String createTime;
    @Expose
    private String creator;
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String pic;
    @Expose
    private String playCount;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    @Override
    public String toString() {
        return "LiteOnlineSongListItem{" +
                "createTime='" + createTime + '\'' +
                ", creator='" + creator + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", playCount='" + playCount + '\'' +
                '}';
    }
}
