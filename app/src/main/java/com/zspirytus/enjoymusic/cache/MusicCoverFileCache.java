package com.zspirytus.enjoymusic.cache;

import android.graphics.Bitmap;
import android.util.SparseArray;

import com.zspirytus.enjoymusic.utils.BitmapUtil;

import java.io.File;

/**
 * Created by ZSpirytus on 2018/8/25.
 */

public class MusicCoverFileCache {

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
            bitmap = BitmapUtil.compressCenterCrop(path);
            mCoverCache.put(key, bitmap);
        }
        return bitmap;
    }

}
