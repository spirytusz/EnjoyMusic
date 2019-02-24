package com.zspirytus.enjoymusic.entity;

import com.zspirytus.enjoymusic.db.table.Music;

public class MusicPickItem {

    private MusicPickItem() {
    }

    private Music music;
    private boolean isSelected;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static MusicPickItem create(Music music) {
        MusicPickItem item = new MusicPickItem();
        item.setMusic(music);
        item.setSelected(false);
        return item;
    }
}
