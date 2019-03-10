package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

public class VisualizerView extends AppCompatImageView {

    private Path path;
    private Paint paint;

    @ColorInt
    private int strokenColor;
    private float strokenWidth;
    private float anchorRadius;
    private float margin;

    public VisualizerView(Context context) {
        this(context, null);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.VisualizerView);
        strokenColor = array.getColor(R.styleable.VisualizerView_strokenColor, Color.WHITE);
        strokenWidth = array.getFloat(R.styleable.VisualizerView_strokenWidth, 1.0f);
        anchorRadius = array.getDimension(R.styleable.VisualizerView_anchorRadius, PixelsUtil.dp2px(context, 100));
        margin = array.getDimension(R.styleable.VisualizerView_anchorMargin, PixelsUtil.dp2px(context, 6));
        array.recycle();

        path = new Path();

        paint = new Paint();
        paint.setColor(strokenColor);
        paint.setPathEffect(new CornerPathEffect(20));
        paint.setStrokeWidth(strokenWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    public void setFrequencies(int[] frequencies) {
        createPath(frequencies);
        invalidate();
    }

    public void setStrokenColor(int strokenColor) {
        this.strokenColor = strokenColor;
    }

    public void setStrokenWidth(int strokenWidth) {
        this.strokenWidth = strokenWidth;
    }

    private void createPath(int[] frequencies) {
        int offsetX = (getLeft() + getRight()) / 2;
        int offsetY = (getTop() + getBottom()) / 2;
        path.reset();
        double angle = 1.0 * Math.PI / frequencies.length;
        int valuess = PixelsUtil.dp2px(getContext(), frequencies[0] * 2);
        path.moveTo((float) (offsetX + (anchorRadius + margin + valuess) * Math.cos(0.0)), (float) (offsetY + (anchorRadius + margin + valuess) * Math.sin(0.0)));
        for (int i = 0; i < 2 * (frequencies.length - 1); i++) {
            if (i % 2 == 0) {
                path.lineTo(
                        (float) ((anchorRadius + margin) * Math.cos(angle * (i + 1))) + offsetX,
                        (float) ((anchorRadius + margin) * Math.sin(angle * (i + 1))) + offsetY
                );
            } else {
                int value = PixelsUtil.dp2px(getContext(), frequencies[(i + 1) / 2]);
                path.lineTo(
                        (float) ((anchorRadius + margin + value * 2) * Math.cos(angle * (i + 1))) + offsetX,
                        (float) ((anchorRadius + margin + value * 2) * Math.sin(angle * (i + 1))) + offsetY
                );
            }
        }
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
