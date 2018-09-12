package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zspirytus.enjoymusic.listeners.OnMultiEventImageViewListener;

/**
 * Created by ZSpirytus on 2018/9/11.
 */

public class MultiEventImageView extends AppCompatImageView implements View.OnTouchListener {

    private OnMultiEventImageViewListener mListener;

    private int mWidth;
    private float mBorder;
    private float mTolerance;

    private float mDownLocationX;
    private boolean hasMoved = false;
    private boolean moveToLeft = false;
    private boolean moveToRight = false;

    public MultiEventImageView(Context context) {
        this(context, null);
    }

    public MultiEventImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiEventImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.post(new Runnable() {
            @Override
            public void run() {
                mWidth = getWidth();
                mBorder = mWidth * 0.618f / 2.0f;
                mTolerance = mWidth * 0.145f / 2.0f;
            }
        });
        setOnTouchListener(this);
    }

    public void setOnMultiEventImageViewListener(OnMultiEventImageViewListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mListener != null) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    mDownLocationX = event.getRawX();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = mDownLocationX - event.getRawX();
                    if (Math.abs(deltaX) > mTolerance) {
                        hasMoved = true;
                        if (deltaX >= mBorder) {
                            moveToLeft = true;
                        } else {
                            moveToLeft = false;
                        }
                        if (deltaX <= -mBorder) {
                            moveToRight = true;
                        } else {
                            moveToRight = false;
                        }
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    if (hasMoved) {
                        if (moveToLeft) {
                            mListener.onMoveToLeft();
                            return true;
                        }
                        if (moveToRight) {
                            mListener.onMoveToRight();
                            return true;
                        }
                    } else {
                        mListener.onClick();
                        return true;
                    }
                    mDownLocationX = 0;
                    hasMoved = false;
                    moveToLeft = false;
                    moveToRight = false;
                    break;
            }
        }
        return false;
    }

}
