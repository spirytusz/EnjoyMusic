package com.zspirytus.enjoymusic.cache;

import android.graphics.Bitmap;
import android.util.SparseArray;

/**
 * Created by ZSpirytus on 2018/8/25.
 */

public class MusicCoverCache {

    private static MusicCoverCache INSTANCE = new MusicCoverCache();

    private SparseArray<Bitmap> mCoverCache = new SparseArray<>();

    private MusicCoverCache() {

    }

    public static MusicCoverCache getInstance() {
        return INSTANCE;
    }

    public void put(int key, Bitmap value) {
        mCoverCache.put(key, value);
    }

    public Bitmap get(int key) {
        return mCoverCache.get(key);
    }
}
