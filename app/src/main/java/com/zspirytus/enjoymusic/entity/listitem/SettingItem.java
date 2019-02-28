package com.zspirytus.enjoymusic.entity.listitem;

public class SettingItem {

    private boolean isTitle = false;
    private String title;

    private boolean isAudioEffect = false;
    private AudioEffectItem audioEffectItem;

    private boolean isDividerLine = false;

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAudioEffect() {
        return isAudioEffect;
    }

    public void setAudioEffect(boolean audioEffect) {
        isAudioEffect = audioEffect;
    }

    public AudioEffectItem getAudioEffectItem() {
        return audioEffectItem;
    }

    public void setAudioEffectItem(AudioEffectItem audioEffectItem) {
        this.audioEffectItem = audioEffectItem;
    }

    public boolean isDividerLine() {
        return isDividerLine;
    }

    public void setDividerLine(boolean dividerLine) {
        isDividerLine = dividerLine;
    }
}
