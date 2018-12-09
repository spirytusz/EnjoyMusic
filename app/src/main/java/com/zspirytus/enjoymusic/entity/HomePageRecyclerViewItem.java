package com.zspirytus.enjoymusic.entity;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class HomePageRecyclerViewItem {

    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;

    private int mItemType;

    private Music mMusic;

    public HomePageRecyclerViewItem() {
        mItemType = ITEM_TYPE_HEADER;
    }

    public HomePageRecyclerViewItem(Music music) {
        mMusic = music;
        mItemType = ITEM_TYPE_CONTENT;
    }

    public int getmItemType() {
        return mItemType;
    }

    public Music getmMusic() {
        return mMusic;
    }
}
