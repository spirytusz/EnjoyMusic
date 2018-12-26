package com.zspirytus.enjoymusic.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class DrawableUtil {

    private DrawableUtil() {
        throw new AssertionError();
    }

    public static Drawable setColor(Context context, int resId, int color) {
        Drawable drawable = context.getDrawable(resId);
        if (drawable != null) {
            DrawableCompat.setTint(drawable, color);
        }
        return drawable;
    }

    public static Drawable setColor(Drawable drawable, int color) {
        if (drawable != null) {
            DrawableCompat.setTint(drawable, color);
        }
        return drawable;
    }
}
