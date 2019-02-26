package com.zspirytus.enjoymusic.entity.listitem;

import com.zspirytus.enjoymusic.db.table.Music;

public class PlayHistoryItem extends Music {

    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PlayHistoryItem(Music music) {
        setMusicName(music.getMusicName());
        setMusicDuration(music.getMusicDuration());
    }
}
