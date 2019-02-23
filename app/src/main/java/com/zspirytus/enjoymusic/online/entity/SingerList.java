
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class SingerList {

    @Expose
    private Long id;
    @Expose
    private String mid;
    @Expose
    private String name;
    @SerializedName("name_hilight")
    private String nameHilight;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameHilight() {
        return nameHilight;
    }

    public void setNameHilight(String nameHilight) {
        this.nameHilight = nameHilight;
    }

}
