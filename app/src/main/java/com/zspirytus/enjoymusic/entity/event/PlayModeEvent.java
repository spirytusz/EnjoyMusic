package com.zspirytus.enjoymusic.entity.event;

public class PlayModeEvent {

    private int playMode;
    private boolean isFromUser;

    public PlayModeEvent(int playMode, boolean isFromUser) {
        this.playMode = playMode;
        this.isFromUser = isFromUser;
    }

    public int getPlayMode() {
        return playMode;
    }

    public boolean isFromUser() {
        return isFromUser;
    }
}
