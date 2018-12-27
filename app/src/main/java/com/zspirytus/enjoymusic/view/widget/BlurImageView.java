package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.utils.BitmapUtil;

import java.io.File;

public class BlurImageView extends AppCompatImageView {

    private boolean isAnimationPlaying = false;
    private Animation disappear;
    private Animation appear;

    public BlurImageView(Context context) {
        this(context, null);
    }

    public BlurImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        disappear = AnimationUtils.loadAnimation(context, R.anim.alpha_to_zero);
        appear = AnimationUtils.loadAnimation(context, R.anim.alpha_to_full);
    }

    public void setImagePath(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                Bitmap source = BitmapFactory.decodeFile(path);
                Bitmap blurBitmap = BitmapUtil.bitmapBlur(getContext(), source, 25);
                setImagePathWithAnimation(blurBitmap);
            }
        }
    }

    private void setImagePathWithAnimation(Bitmap newBitmap) {
        if (isAnimationPlaying) {
            disappear.cancel();
            appear.cancel();
        }
        disappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                GlideApp.with(BlurImageView.this)
                        .load(newBitmap)
                        .into(BlurImageView.this);
                BlurImageView.this.startAnimation(appear);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        appear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationPlaying = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.startAnimation(disappear);
        isAnimationPlaying = true;
    }

    public Bitmap getImageBitmap() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            Bitmap bitmap = BitmapUtil.drawableToBitmap(drawable);
            return bitmap;
        } else {
            return null;
        }
    }
}
