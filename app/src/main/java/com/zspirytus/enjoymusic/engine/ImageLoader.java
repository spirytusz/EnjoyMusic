package com.zspirytus.enjoymusic.engine;

import android.graphics.Typeface;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;
import com.zspirytus.enjoymusic.view.widget.TextDrawable;

import java.io.File;

public class ImageLoader {

    private ImageLoader() {
        throw new AssertionError();
    }

    public static void load(ImageView imageView, String path, String alternativeText) {
        if (path != null) {
            File file = MusicCoverFileCache.getInstance().getCoverFile(path);
            if (file != null) {
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(file)
                        .into(imageView);
            } else {
                loadTextDrawable(imageView, alternativeText);
            }
        } else {
            loadTextDrawable(imageView, alternativeText);
        }
    }

    @SuppressWarnings("unchecked")
    public static void load(ImageView imageView, String path, String alternativeText, BitmapTransformation... transformations) {
        MultiTransformation multiTransformation = new MultiTransformation(transformations);
        RequestOptions options = new RequestOptions().transform(multiTransformation);
        if (path != null) {
            File file = MusicCoverFileCache.getInstance().getCoverFile(path);
            if (file != null) {
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(file)
                        .apply(options)
                        .into(imageView);
            } else {
                loadTextDrawable(imageView, alternativeText);
            }
        } else {
            loadTextDrawable(imageView, alternativeText);
        }
    }

    public static void load(ImageView imageView, int imageResourceId) {
        GlideApp.with(MainApplication.getForegroundContext())
                .load(imageResourceId)
                .into(imageView);
    }

    @SuppressWarnings("unchecked")
    public static void load(ImageView imageView, int imageResourceId, BitmapTransformation... transformations) {
        MultiTransformation multiTransformation = new MultiTransformation(transformations);
        RequestOptions options = new RequestOptions().transform(multiTransformation);
        GlideApp.with(MainApplication.getForegroundContext())
                .load(imageResourceId)
                .apply(options)
                .into(imageView);
    }

    private static void loadTextDrawable(ImageView imageView, String alternativeText) {
        if (alternativeText == null || alternativeText.length() < 2) {
            alternativeText = "未知";
        } else {
            alternativeText = alternativeText.replaceAll(" ", "");
            alternativeText = alternativeText.substring(0, 2);
        }
        final String text = alternativeText;
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                TextDrawable drawable = TextDrawable.builder()
                        .beginConfig()
                        .textColor(0x88FFFFFF)
                        .useFont(Typeface.DEFAULT)
                        .fontSize((int) (imageView.getWidth() * 0.618f))
                        .bold()
                        .endConfig()
                        .buildRect(text);
                imageView.setImageDrawable(drawable);
            }
        });
    }
}
