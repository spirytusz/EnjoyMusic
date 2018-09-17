
package com.zspirytus.enjoymusic.entity.songinfo;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class Body {

    @Expose
    private String author;
    @Expose
    private String lrc;
    @Expose
    private String pic;
    @Expose
    private Long time;
    @Expose
    private String title;
    @Expose
    private String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLrc() {
        return lrc;
    }

    public void setLrc(String lrc) {
        this.lrc = lrc;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
