package com.zspirytus.enjoymusic.entity.listitem;

public class SettingItem {

    private String title;

    private boolean hasSwitch = false;
    private boolean isChecked = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasSwitch() {
        return hasSwitch;
    }

    public void setHasSwitch(boolean hasSwitch) {
        this.hasSwitch = hasSwitch;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
