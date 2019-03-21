package com.zspirytus.basesdk.entity;

public class SelectableItem<T> {

    private T t;
    private boolean isSelected;

    public SelectableItem(T t) {
        this.t = t;
        this.isSelected = false;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
