package com.zspirytus.enjoymusic.entity;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class WrapLyricRow {

    private LyricRow row;
    private StaticLayout staticLayout;
    private float offset = Float.MIN_VALUE;

    public WrapLyricRow(LyricRow row, TextPaint paint, int width) {
        this.row = row;
        staticLayout = new StaticLayout(
                row.getText(),
                paint,
                width,
                Layout.Alignment.ALIGN_CENTER,
                1f,
                0f,
                false
        );
    }

    public LyricRow getRow() {
        return row;
    }

    public StaticLayout getStaticLayout() {
        return staticLayout;
    }

    public void setStaticLayout(StaticLayout staticLayout) {
        if (this.staticLayout == null) {
            this.staticLayout = staticLayout;
        }
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    public int getRowHeight() {
        return staticLayout != null ? staticLayout.getHeight() : 0;
    }
}
