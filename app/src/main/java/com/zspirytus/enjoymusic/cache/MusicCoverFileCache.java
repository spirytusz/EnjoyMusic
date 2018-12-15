package com.zspirytus.enjoymusic.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import java.io.File;

/**
 * Created by ZSpirytus on 2018/8/25.
 */

public class MusicCoverFileCache {

    private static final int TARGET_WIDTH = 96;
    private static final int TARGET_HEIGHT = 96;

    private static MusicCoverFileCache INSTANCE = new MusicCoverFileCache();

    private SparseArray<File> mCoverFileCache;
    private SparseArray<Bitmap> mCoverCache;

    private MusicCoverFileCache() {
        mCoverFileCache = new SparseArray<>();
        mCoverCache = new SparseArray<>();
    }

    public static MusicCoverFileCache getInstance() {
        return INSTANCE;
    }

    public File getCoverFile(String path) {
        int key = path.hashCode();
        File file = mCoverFileCache.get(key);
        if (file == null) {
            file = new File(path);
            mCoverFileCache.put(key, file);
        }
        return file;
    }

    public Bitmap getCover(String path) {
        int key = path.hashCode();
        Bitmap bitmap = mCoverCache.get(key);
        if (bitmap == null) {
            bitmap = compressBitmap(path);
            mCoverCache.put(key, bitmap);
        }
        return bitmap;
    }

    private Bitmap compressBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        options.inJustDecodeBounds = false;
        options.inSampleSize = computeInSampleSize(width, height);
        return BitmapFactory.decodeFile(path, options);
    }

    private int computeInSampleSize(int width, int height) {
        if (width == height) {
            return width / TARGET_WIDTH;
        } else if (width > height) {
            return height / TARGET_HEIGHT;
        } else {
            return width / TARGET_WIDTH;
        }
    }
}
