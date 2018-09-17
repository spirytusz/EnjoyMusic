package com.zspirytus.enjoymusic.entity;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class HomePageChildRecyclerViewItem {

    private String imgPath;
    private String title;
    private String subTitle;

    public HomePageChildRecyclerViewItem(String imgPath, String title, String subTitle) {
        this.imgPath = imgPath;
        this.title = title;
        this.subTitle = subTitle;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public String toString() {
        return "{ imgPath = " + (imgPath != null ? imgPath : "null")
                + ", title = " + (title != null ? title : "null")
                + ", subTitle = " + (subTitle != null ? subTitle : "null")
                + "}";
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof HomePageChildRecyclerViewItem && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
