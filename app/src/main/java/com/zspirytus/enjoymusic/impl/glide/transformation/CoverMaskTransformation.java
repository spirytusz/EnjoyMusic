package com.zspirytus.enjoymusic.impl.glide.transformation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.utils.BitmapUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class CoverMaskTransformation extends BitmapTransformation {

    private static String ID;
    private Context mContext;

    public CoverMaskTransformation(Context context) {
        mContext = context;
        ID = "com.zspirytus.enjoymusic.impl.glide.transformation.CoverMaskTransformation Context = " + context;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Drawable playIcon = mContext.getDrawable(R.drawable.ic_volume_up_black_24dp);
        playIcon.setTint(mContext.getResources().getColor(R.color.colorPrimary));
        Drawable mask = mContext.getDrawable(R.drawable.cover_mask);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), toTransform);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                bitmapDrawable,
                mask,
                playIcon
        });
        layerDrawable.setBounds(0, 0, outWidth, outHeight);
        Canvas canvas = new Canvas(toTransform);
        layerDrawable.draw(canvas);
        return BitmapUtil.drawableToBitmap(layerDrawable);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        try {
            messageDigest.update(ID.getBytes(STRING_CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CoverMaskTransformation) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }
}
