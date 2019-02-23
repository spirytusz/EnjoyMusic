package com.zspirytus.enjoymusic.online.entity;

@SuppressWarnings("unused")
public class OnlineMusic {

    private String id;
    private String lrc;
    private String name;
    private String pic;
    private String singer;
    private Long time;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "OnlineMusic{" +
                "id='" + id + '\'' +
                ", lrc='" + lrc + '\'' +
                ", name='" + name + '\'' +
                ", pic='" + pic + '\'' +
                ", singer='" + singer + '\'' +
                ", time=" + time +
                ", url='" + url + '\'' +
                '}';
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
