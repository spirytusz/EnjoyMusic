package com.zspirytus.enjoymusic.cache;

import android.graphics.Bitmap;
import android.util.SparseArray;

import com.zspirytus.enjoymusic.R;
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
        if (path != null) {
            int key = path.hashCode();
            File file = mCoverFileCache.get(key);
            if (file == null) {
                file = new File(path);
                if (file.exists()) {
                    mCoverFileCache.put(key, file);
                }
            }
            return file;
        } else {
            return null;
        }
    }

    public Bitmap getCover(String path) {
        if (path != null) {
            File file = new File(path);
            if (!file.exists()) {
                return getCover(R.drawable.defalut_cover);
            }
            int key = path.hashCode();
            Bitmap bitmap = mCoverCache.get(key);
            if (bitmap == null) {
                bitmap = BitmapUtil.compressCenterCrop(path);
                mCoverCache.put(key, bitmap);
            }
            return bitmap;
        } else {
            return getCover(R.drawable.defalut_cover);
        }
    }

    public Bitmap getCover(int resId) {
        if (resId != 0) {
            String stringValue = resId + "";
            Bitmap bitmap = mCoverCache.get(stringValue.hashCode());
            if (bitmap == null) {
                bitmap = BitmapUtil.createBitmapByResId(resId);
                mCoverCache.put(stringValue.hashCode(), bitmap);
            }
            return bitmap;
        } else {
            return null;
        }
    }

}
