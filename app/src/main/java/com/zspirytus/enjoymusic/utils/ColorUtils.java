package com.zspirytus.enjoymusic.utils;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.enjoymusic.global.MainApplication;

public class ColorUtils {

    private ColorUtils() {
        throw new AssertionError();
    }

    private static int mCurrentColor = Color.parseColor("#FFFFFF");

    public static void setSelfAdaptionDarkMutedColor(Bitmap source, View... views) {
        if (source != null) {
            Palette.from(source).generate((palette -> {
                Palette.Swatch swatch = palette.getDarkMutedSwatch();
                if (swatch != null) {
                    setColor(swatch.getRgb(), views);
                } else {
                    setColor(Color.parseColor("#FFFFFF"), views);
                }
            }));
        }
    }

    public static void colorAnimation(int fromColor, int toColor, ColorListener listener) {
        ValueAnimator animator = ValueAnimator.ofArgb(fromColor, toColor);
        animator.setDuration(618);
        animator.addUpdateListener((valueAnimator) -> {
            int color = (int) valueAnimator.getAnimatedValue();
            listener.onColorChnage(color);
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    public static void setCurrentColor(int id, ImageView imageView) {
        Drawable drawable = MainApplication.getForegroundContext().getDrawable(id);
        drawable = DrawableUtil.setColor(drawable, mCurrentColor);
        imageView.setImageDrawable(drawable);
    }

    private static void setColor(int color, View... views) {
        if (color != 0) {
            mCurrentColor = color;
            for (View view : views) {
                if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;
                    Drawable drawable = imageView.getDrawable();
                    if (drawable != null) {
                        drawable = DrawableUtil.setColor(drawable, color);
                        ((ImageView) view).setImageDrawable(drawable);
                    }
                } else if (view instanceof TextView) {
                    ((TextView) view).setTextColor(color);
                }
            }
        } else {
            mCurrentColor = Color.parseColor("#FFFFFF");
        }
    }

    public static interface ColorListener {
        void onColorChnage(int rgb);
    }
}
