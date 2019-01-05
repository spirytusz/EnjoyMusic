package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.zspirytus.enjoymusic.impl.glide.GlideApp;
import com.zspirytus.enjoymusic.impl.glide.transformation.BlurTransformation;
import com.zspirytus.enjoymusic.impl.glide.transformation.GradientTransformation;
import com.zspirytus.enjoymusic.utils.BitmapUtil;

import java.io.File;

public class BlurImageView extends AppCompatImageView {

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("unchecked")
    public void setImagePath(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                GlideApp.with(BlurImageView.this)
                        .load(file)
                        .transform(new MultiTransformation(
                                new CenterCrop(),
                                new GradientTransformation(getContext()),
                                new BlurTransformation(getContext(), 25, 16)
                        ))
                        .transition(new DrawableTransitionOptions().crossFade(1000))
                        .into(BlurImageView.this);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setImageResource(int resId) {
        GlideApp.with(BlurImageView.this)
                .load(resId)
                .transform(new MultiTransformation(
                        new CenterCrop(),
                        new GradientTransformation(getContext()),
                        new BlurTransformation(getContext(), 25, 16)
                ))
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
