package com.zspirytus.enjoymusic.engine;

import android.widget.ImageView;

import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
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
                GlideApp.with(imageView)
                        .load(file)
                        .into(imageView);
            } else {
                GlideApp.with(imageView)
                        .load(defaultRes)
                        .into(imageView);
            }
        } else {
            GlideApp.with(imageView)
                    .load(defaultRes)
                    .into(imageView);
        }
    }
}