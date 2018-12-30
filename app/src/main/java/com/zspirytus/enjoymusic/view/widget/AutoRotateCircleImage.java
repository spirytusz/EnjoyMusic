package com.zspirytus.enjoymusic.view.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import com.zspirytus.enjoymusic.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AutoRotateCircleImage extends CircleImageView {

    private ObjectAnimator mRotateAnim;

    private int T;

    public AutoRotateCircleImage(Context context) {
        this(context, null);
    }

    public AutoRotateCircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoRotateCircleImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AutoRotateCircleImage);
            T = array.getInteger(R.styleable.AutoRotateCircleImage_cycle_seconds, 24) * 1000;
            array.recycle();
        }
        initAnim();
    }

    private void initAnim() {
        mRotateAnim = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        mRotateAnim.setDuration(T);
        mRotateAnim.setInterpolator(new LinearInterpolator());
        mRotateAnim.setRepeatCount(ObjectAnimator.INFINITE);
        mRotateAnim.setRepeatMode(ObjectAnimator.RESTART);
    }

    public void setRotating(boolean isRotating) {
        if (isRotating) {
            if (mRotateAnim.isStarted()) {
                mRotateAnim.resume();
            } else {
                mRotateAnim.start();
            }
        } else {
            if (mRotateAnim.isStarted()) {
                mRotateAnim.pause();
            }
        }
    }

    public void resetRotation() {
        if (mRotateAnim.isStarted()) {
            mRotateAnim.end();
        }
        initAnim();
    }
}
