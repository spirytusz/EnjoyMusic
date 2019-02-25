package com.zspirytus.enjoymusic.engine;

import android.graphics.Typeface;
import android.widget.ImageView;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;
import com.zspirytus.enjoymusic.view.widget.TextDrawable;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageLoader {

    private ImageLoader() {
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    public static void load(ImageView imageView, String path, String alternativeText, BitmapTransformation... transformations) {
        RequestOptions options = new RequestOptions();
        if (transformations != null && transformations.length > 0) {
            MultiTransformation multiTransformation = new MultiTransformation(transformations);
            options = options.transform(multiTransformation);
        }
        if (path != null) {
            File file = MusicCoverFileCache.getInstance().getCoverFile(path);
            if (file != null) {
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(file)
                        .apply(options)
                        .into(imageView);
            } else if (path.contains("http") || path.contains("https")) {
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(path)
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

    private static void loadTextDrawable(ImageView imageView, String text) {
        String s = text;
        if (s == null || s.length() < 2) {
            s = "未知";
        } else {
            s = s.replaceAll(" ", "");
            s = s.substring(0, 2);
        }
        final String ss = s;
        imageView.post(() -> {
            TextDrawable.IConfigBuilder builder = TextDrawable.builder()
                    .beginConfig()
                    .textColor(0x88FFFFFF)
                    .useFont(Typeface.DEFAULT)
                    .bold();
            if (imageView instanceof CircleImageView) {
                int width = imageView.getWidth();
                int height = imageView.getHeight();
                builder.width(width).height(height);
            }
            TextDrawable drawable = builder.endConfig().buildRect(ss);
            imageView.setImageDrawable(drawable);
        });
    }
}
