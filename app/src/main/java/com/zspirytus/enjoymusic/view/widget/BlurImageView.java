package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.impl.glide.BlurTransformation;
import com.zspirytus.enjoymusic.utils.BitmapUtil;

import java.io.File;

public class BlurImageView extends AppCompatImageView {

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImagePath(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                GlideApp.with(BlurImageView.this)
                        .load(file)
                        .centerCrop()
                        .transform(new BlurTransformation(getContext(), 25, 16))
                        .transition(new DrawableTransitionOptions().crossFade(1000))
                        .into(BlurImageView.this);
            }
        }
    }

    @Override
    public void setImageResource(int resId) {
        GlideApp.with(BlurImageView.this)
                .load(resId)
                .centerCrop()
                .transform(new BlurTransformation(getContext(), 25, 16))
                .transition(new DrawableTransitionOptions().crossFade(1000))
                .into(BlurImageView.this);
    }

    public Bitmap getImageBitmap() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            return BitmapUtil.drawableToBitmap(drawable);
        } else {
            return null;
        }
    }
}
