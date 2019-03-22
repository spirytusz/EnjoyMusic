package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.enjoymusic.R;

/**
 * Created by ZSpirytus on 2018/8/3.
 */

public class RoundCornerImageView extends AppCompatImageView {

    private int DEFAULT_DP;

    private Path path;

    private float height;
    private float width;

    private int topLeft;
    private int topRight;
    private int bottomLeft;
    private int bottomRight;

    public RoundCornerImageView(Context context) {
        this(context, null);
    }

    public RoundCornerImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundCornerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        path = new Path();
        DEFAULT_DP = PixelsUtil.dp2px(context, 8);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerImageView);
        topLeft = array.getDimensionPixelSize(R.styleable.RoundCornerImageView_topLeft, DEFAULT_DP);
        topRight = array.getDimensionPixelSize(R.styleable.RoundCornerImageView_topRight, DEFAULT_DP);
        bottomLeft = array.getDimensionPixelSize(R.styleable.RoundCornerImageView_bottomLeft, DEFAULT_DP);
        bottomRight = array.getDimensionPixelSize(R.styleable.RoundCornerImageView_bottomRight, DEFAULT_DP);
        array.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        path.moveTo(topLeft, 0);

        // top right
        path.lineTo(width - topRight, 0);
        path.quadTo(width, 0, width, topRight);

        //  bottom right
        path.lineTo(width, height - bottomRight);
        path.quadTo(width, height, width - bottomRight, height);

        // bottom left
        path.lineTo(bottomLeft, height);
        path.quadTo(0, height, 0, height - bottomLeft);

        // top left
        path.lineTo(0, topLeft);
        path.quadTo(0, 0, topLeft, 0);

            canvas.clipPath(path);
        path.reset();
        super.onDraw(canvas);
    }

    private int getMax() {
        int t = Math.max(topLeft, topRight);
        t = Math.max(t, bottomLeft);
        t = Math.max(t, bottomRight);
        return t;
    }

}
