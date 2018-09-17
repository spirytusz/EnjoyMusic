package com.zspirytus.enjoymusic.entity;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class HomePageRecyclerViewItem {

    private String title;
    private List<HomePageChildRecyclerViewItem> homePageChildRecyclerViewItems;

    public HomePageRecyclerViewItem(String title, List<HomePageChildRecyclerViewItem> homePageChildRecyclerViewItems) {
        this.title = title;
        this.homePageChildRecyclerViewItems = homePageChildRecyclerViewItems;
    }

    public String getTitle() {
        return title;
    }

    public List<HomePageChildRecyclerViewItem> getHomePageChildRecyclerViewItems() {
        return homePageChildRecyclerViewItems;
    }
}
