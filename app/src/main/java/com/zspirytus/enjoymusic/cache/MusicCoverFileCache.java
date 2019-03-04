package com.zspirytus.enjoymusic.cache;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.SparseArray;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.utils.BitmapUtil;
import com.zspirytus.enjoymusic.view.widget.TextDrawable;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by ZSpirytus on 2018/8/25.
 */

public class MusicCoverFileCache {

    private static MusicCoverFileCache INSTANCE = new MusicCoverFileCache();

    private SparseArray<WeakReference<File>> mCoverFileCache;
    private SparseArray<WeakReference<Bitmap>> mCoverCache;

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
            WeakReference<File> weakReference = mCoverFileCache.get(key);
            File file;
            if (weakReference == null || (file = weakReference.get()) == null) {
                file = new File(path);
                if (file.exists()) {
                    WeakReference<File> fileWeakReference = new WeakReference<>(file);
                    mCoverFileCache.put(key, fileWeakReference);
                }
            }
            return file;
        } else {
            return null;
        }
    }

    public Bitmap getCoverBitmap(Album album) {
        String path = album.getAlbumArt();
        if (path != null) {
            File file = new File(path);
            if (!file.exists()) {
                return getCoverBitmap(album.getAlbumName());
            }
            int key = path.hashCode();
            WeakReference<Bitmap> weakReference = mCoverCache.get(key);
            Bitmap bitmap;
            if (weakReference == null || (bitmap = weakReference.get()) == null) {
                bitmap = BitmapUtil.compressCenterCrop(path);
                WeakReference<Bitmap> bitmapWeakReference = new WeakReference<>(bitmap);
                mCoverCache.put(key, bitmapWeakReference);
            }
            return bitmap;
        } else {
            return getCoverBitmap(album.getAlbumName());
        }
    }

    private Bitmap getCoverBitmap(String albumName) {
        final int width = 300;
        final int height = 300;
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .textColor(0x44FFFFFF)
                .useFont(Typeface.DEFAULT)
                .bold()
                .width(width)
                .height(height)
                .endConfig()
                .buildRect(albumName.substring(0, albumName.length() >= 2 ? 2 : albumName.length()));
        return BitmapUtil.drawableToBitmap(textDrawable);
    }

}
