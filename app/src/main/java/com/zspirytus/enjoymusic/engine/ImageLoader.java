package com.zspirytus.enjoymusic.engine;

import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;

import java.io.File;

public class ImageLoader {

    private ImageLoader() {
        throw new AssertionError();
    }

    public static void load(ImageView imageView, String path, int defaultRes) {
        if (path != null) {
            File file = MusicCoverFileCache.getInstance().getCoverFile(path);
            if (file != null) {
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(file)
                        .into(imageView);
            } else {
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(defaultRes)
                        .into(imageView);
            }
        } else {
            GlideApp.with(MainApplication.getForegroundContext())
                    .load(defaultRes)
                    .into(imageView);
        }
    }

    @SuppressWarnings("unchecked")
    public static void load(ImageView imageView, String path, int defaultRes, BitmapTransformation... transformations) {
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
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(defaultRes)
                        .apply(options)
                        .into(imageView);
            }
        } else {
            GlideApp.with(MainApplication.getForegroundContext())
                    .load(defaultRes)
                    .apply(options)
                    .into(imageView);
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
}
