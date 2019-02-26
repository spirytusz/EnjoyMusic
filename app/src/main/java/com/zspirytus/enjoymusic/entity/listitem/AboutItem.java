package com.zspirytus.enjoymusic.entity.listitem;

public class AboutItem {

    private boolean isTitle = false;
    private String title;

    private boolean isCommonItem = false;
    private boolean isOnlyMainTitle = false;
    private String mainTitle;
    private String subTitle;

    private String extraInfo;

    public boolean isTitle() {
        return isTitle;
    }

    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isOnlyMainTitle() {
        return isOnlyMainTitle;
    }

    public void setOnlyMainTitle(boolean onlyMainTitle) {
        isOnlyMainTitle = onlyMainTitle;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public boolean isCommonItem() {
        return isCommonItem;
    }

    public void setCommonItem(boolean commonItem) {
        isCommonItem = commonItem;
    }
}
