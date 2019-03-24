package com.zspirytus.enjoymusic.entity.listitem;

public class AudioEffectItem {
    private String title;
    private boolean isSingleEffect;
    private boolean isChecked;
    /*
     * AcousticEchoCanceler     -> 0
     * AutomaticGainControl     -> 1
     * NoiseSuppressor          -> 2
     * other                    -> 3
     */
    private int flag = 3;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSingleEffect() {
        return isSingleEffect;
    }

    public void setSingleEffect(boolean singleEffect) {
        isSingleEffect = singleEffect;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
