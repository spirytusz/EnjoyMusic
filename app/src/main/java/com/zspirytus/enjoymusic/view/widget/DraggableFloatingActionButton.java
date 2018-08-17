package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.listeners.OnDraggableFABEventListener;

/**
 * Created by ZSpirytus on 2018/8/17.
 */

public class DraggableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    private final static float CLICK_DRAG_TOLERANCE = 61.8f;
    private float damping = 0.618f;
    private float border;

    private static float initRawX;
    private float downRawX;
    private float dX;
    private float deltaX;

    private OnDraggableFABEventListener onDraggableFABEventListener;

    public DraggableFloatingActionButton(Context context) {
        this(context, null);
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttrs(context, attrs);
        init();
    }

    public void setOnDraggableFABEventListener(OnDraggableFABEventListener onDraggableFABEventListener) {
        this.onDraggableFABEventListener = onDraggableFABEventListener;
    }

    private void loadAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DraggableFloatingActionButton);
            damping = array.getFloat(R.styleable.DraggableFloatingActionButton_damping, 0.618f);
            border = array.getDimension(R.styleable.DraggableFloatingActionButton_border, 200f);
            array.recycle();
        }
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        initRawX = getX();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.getRawX();
            dX = view.getX() - downRawX;
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            int viewWidth = view.getWidth();

            View viewParent = (View) view.getParent();
            int parentWidth = viewParent.getWidth();

            deltaX = motionEvent.getRawX() + dX;
            deltaX = Math.max(0, deltaX);
            deltaX = Math.min(parentWidth - viewWidth, deltaX);
            deltaX = (deltaX - initRawX) * damping;
            if (deltaX > border) {
                deltaX = border;
            } else if (deltaX < -border) {
                deltaX = -border;
            }
            // compute delta X and multiply damping

            view.animate()
                    .x(initRawX + deltaX)
                    .setDuration(0)
                    .start();
            // show animator
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            float upRawX = motionEvent.getRawX();
            float upDX = upRawX - downRawX;

            view.animate()
                    .x(initRawX)
                    .setDuration(382)
                    .start();
            // show reset place animator

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE) {
                if (onDraggableFABEventListener != null) {
                    onDraggableFABEventListener.onClick();
                }
                return performClick();
            } else {
                if (onDraggableFABEventListener != null) {
                    if (deltaX == border) {
                        onDraggableFABEventListener.onDraggedRight();
                    } else if (deltaX == -border) {
                        onDraggableFABEventListener.onDraggedLeft();
                    }
                }
                return true;
            }

        } else {
            return super.onTouchEvent(motionEvent);
        }
    }
}