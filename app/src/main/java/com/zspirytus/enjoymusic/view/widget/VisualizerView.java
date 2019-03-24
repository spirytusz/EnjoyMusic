package com.zspirytus.enjoymusic.view.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.services.media.VisualizerHelper;

public class VisualizerView extends View {

    private static final float DEFAULT_STROKEN_WIDTH = 1.0f;
    private static final int DEFAULT_STROKEN_COLOR = Color.WHITE;
    private static final int DEFAULT_ANCHOR_RADIUS = 100;
    private static final int DEFAULT_MARGIN = 6;
    private static final int DEFAULT_CORNER_RADIUS = 36;
    private static final int DEFAULT_TO_ZERO_DURATION = 300;

    private Path path;
    private Paint paint;

    @ColorInt
    private int strokenColor;
    private float strokenWidth;
    private float anchorRadius;
    private float margin;
    private int cornerRadius;
    private int toZeroDuration;

    /*
     * captureRate, Visualizer每100ms取样一次频率.
     */
    private int captureRate = 100;
    private ValueAnimator mPathShapeAnim;
    private float[] rearFrequencies;
    private float[] currentFrequencies;
    private float[] diff;

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
        strokenColor = array.getColor(R.styleable.VisualizerView_strokenColor, DEFAULT_STROKEN_COLOR);
        strokenWidth = array.getFloat(R.styleable.VisualizerView_strokenWidth, DEFAULT_STROKEN_WIDTH);
        anchorRadius = array.getDimension(R.styleable.VisualizerView_anchorRadius, PixelsUtil.dp2px(context, DEFAULT_ANCHOR_RADIUS));
        margin = array.getDimension(R.styleable.VisualizerView_anchorMargin, PixelsUtil.dp2px(context, DEFAULT_MARGIN));
        cornerRadius = array.getInteger(R.styleable.VisualizerView_cornerRadius, DEFAULT_CORNER_RADIUS);
        toZeroDuration = array.getInteger(R.styleable.VisualizerView_toZeroDuration, DEFAULT_TO_ZERO_DURATION);
        array.recycle();

        path = new Path();

        paint = new Paint();
        paint.setColor(strokenColor);
        paint.setPathEffect(new CornerPathEffect(cornerRadius));
        paint.setStrokeWidth(strokenWidth);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        rearFrequencies = new float[VisualizerHelper.getCaptureSize()];
        currentFrequencies = new float[VisualizerHelper.getCaptureSize()];
        diff = new float[VisualizerHelper.getCaptureSize()];

        mPathShapeAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
        mPathShapeAnim.setDuration(captureRate);
        mPathShapeAnim.setInterpolator(new DecelerateInterpolator());
        mPathShapeAnim.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            setShapeProgress(progress);
            invalidate();
        });
    }

    public void setFrequencies(float[] frequencies) {
        if (getAlpha() != 0.0f) {
            rearFrequencies = currentFrequencies;
            currentFrequencies = frequencies;
            boolean isPaused = calculateDiff();
            if (mPathShapeAnim.isRunning()) {
                mPathShapeAnim.end();
            }
            if (isPaused) {
                mPathShapeAnim.setDuration(toZeroDuration);
            } else {
                mPathShapeAnim.setDuration(captureRate);
            }
            mPathShapeAnim.start();
        }
    }

    public void setStrokenColor(int strokenColor) {
        this.strokenColor = strokenColor;
        paint.setColor(strokenColor);
    }

    public void setStrokenWidth(int strokenWidth) {
        this.strokenWidth = strokenWidth;
        paint.setStrokeWidth(strokenWidth);
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        paint.setPathEffect(new CornerPathEffect(cornerRadius));
    }

    /**
     * 极坐标法绘制示波器
     *
     * @param progress 从rearFrequencies变化到currentFrequencies的百分比
     */
    private void createPath(float progress) {
        int offsetX = (getLeft() + getRight()) >> 1;
        int offsetY = getTop() + getHeight() >> 1;
        float radius = anchorRadius + margin;
        path.reset();
        double angle = 1.0 * Math.PI / rearFrequencies.length;
        int valuess = PixelsUtil.dp2px(getContext(), rearFrequencies[0] + diff[0] * progress);
        path.moveTo((float) (offsetX + (radius + valuess) * Math.cos(0.0)), (float) (offsetY + (radius + valuess) * Math.sin(0.0)));
        for (int i = 0; i < 2 * (rearFrequencies.length - 1); i++) {
            double currentAngle = angle * (i + 1);
            if (i % 2 == 0) {
                path.lineTo(
                        (float) (radius * Math.cos(currentAngle)) + offsetX,
                        (float) (radius * Math.sin(currentAngle)) + offsetY
                );
            } else {
                float value = PixelsUtil.dp2px(getContext(), rearFrequencies[(i + 1) / 2] + diff[(i + 1) / 2] * progress);
                path.lineTo(
                        (float) ((radius + value) * Math.cos(currentAngle)) + offsetX,
                        (float) ((radius + value) * Math.sin(currentAngle)) + offsetY
                );
            }
        }
        path.close();
    }

    private void setShapeProgress(float progress) {
        createPath(progress);
    }

    private boolean calculateDiff() {
        /*
         * 如果是暂停状态, 那么currentFrequencies就应该全为0
         */
        float f = 0f;
        for (int i = 0; i < rearFrequencies.length; i++) {
            diff[i] = currentFrequencies[i] - rearFrequencies[i];
            f += currentFrequencies[i];
        }
        return f == 0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!path.isEmpty()) {
            canvas.drawPath(path, paint);
        } else {
            int x = (getLeft() + getRight()) >> 1;
            int y = getTop() + getHeight() >> 1;
            canvas.drawCircle(x, y, anchorRadius + margin, paint);
        }
    }
}
