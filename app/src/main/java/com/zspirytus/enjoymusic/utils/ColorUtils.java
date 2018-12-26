package com.zspirytus.enjoymusic.utils;

import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;

public class ColorUtils {

    private ColorUtils() {
        throw new AssertionError();
    }

    public static void setSelfAdaptionColor(Bitmap bitmap, OnPaletteCompletedListener listener) {
        Palette.from(bitmap).generate((palette) -> {
            listener.onComplete(palette);
        });
    }

    public interface OnPaletteCompletedListener {
        void onComplete(Palette palette);
    }
}
