package com.zspirytus.enjoymusic.impl.glide;

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

public class GradientTransformation extends BitmapTransformation {

    private static String ID;

    private Context mContext;

    public GradientTransformation(Context context) {
        mContext = context;
        ID = "com.zspirytus.enjoymusic.impl.glide.GradientTransformation context = " + context.toString();
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Drawable gradient = mContext.getDrawable(R.drawable.gradient_center_transparent);
        Drawable bitmapDrawable = new BitmapDrawable(mContext.getResources(), toTransform);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{gradient, bitmapDrawable});
        Canvas canvas = new Canvas(toTransform);
        layerDrawable.setBounds(0, 0, outWidth, outHeight);
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
        if (obj instanceof GradientTransformation) {
            return hashCode() == obj.hashCode();
        }
        return false;
    }
}
