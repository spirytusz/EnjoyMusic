package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.listeners.OnDraggableFABEventListener;
import com.zspirytus.enjoymusic.services.MediaPlayController;

/**
 * Created by ZSpirytus on 2018/8/17.
 */

public class DraggableFloatingActionButton extends FloatingActionButton implements View.OnTouchListener {

    private static final float CLICK_DRAG_TOLERANCE = 38.2f;
    private static final int RESET_ANIMATOR_DURATION = 382;
    private static final int RESPONSE_ACTION_MOVE_DELAY = 0;
    private static final float DEFAULT_DAMPING = 0.618f;
    private static final float DEFAULT_BORDER = 200f;

    private float damping;
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
            damping = array.getFloat(R.styleable.DraggableFloatingActionButton_damping, DEFAULT_DAMPING);
            border = array.getDimension(R.styleable.DraggableFloatingActionButton_border, DEFAULT_BORDER);
            array.recycle();
        }
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        // get mFab initial location X
        initRawX = getX();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downRawX = motionEvent.getRawX();
            dX = initRawX - downRawX;
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            deltaX = motionEvent.getRawX() + dX;
            deltaX = (deltaX - initRawX) * damping;
            if (deltaX < -border) {
                deltaX = -border;
            } else if (deltaX < -CLICK_DRAG_TOLERANCE) {
                setImageResource(R.drawable.ic_skip_previous_white_48dp);
            } else if (deltaX > CLICK_DRAG_TOLERANCE && deltaX <= border) {
                setImageResource(R.drawable.ic_skip_next_white_48dp);
            } else if (deltaX > border) {
                deltaX = border;
            }
            if (Math.abs(deltaX) >= CLICK_DRAG_TOLERANCE) {
                view.animate()
                        .x(initRawX + deltaX)
                        .setDuration(RESPONSE_ACTION_MOVE_DELAY)
                        .start();
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            int resId = MediaPlayController.getInstance().isPlaying() ? R.drawable.ic_pause_white_48dp : R.drawable.ic_play_arrow_white_48dp;
            setImageResource(resId);
            if (Math.abs(deltaX) < CLICK_DRAG_TOLERANCE) {
                if (onDraggableFABEventListener != null) {
                    onDraggableFABEventListener.onClick();
                }
            } else {
                if (onDraggableFABEventListener != null) {
                    if (deltaX == border) {
                        onDraggableFABEventListener.onDraggedRight();
                    } else if (deltaX == -border) {
                        onDraggableFABEventListener.onDraggedLeft();
                    }
                }
            }
            view.animate()
                    .x(initRawX)
                    .setDuration(RESET_ANIMATOR_DURATION)
                    .start();
            return true;

        } else {
            return super.onTouchEvent(motionEvent);
        }
    }
}