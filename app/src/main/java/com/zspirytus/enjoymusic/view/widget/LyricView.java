package com.zspirytus.enjoymusic.view.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.LyricRow;
import com.zspirytus.enjoymusic.entity.WrapLyricRow;

import java.util.ArrayList;
import java.util.List;

public class LyricView extends View {

    private List<WrapLyricRow> mLyricRows;
    private int mCurrentRow;
    private boolean isCenterLineVisible;
    private Scroller mScroller;
    private float mOffset;
    private ValueAnimator mScrollAnimator;
    private TextPaint mLyricPaint;
    private TextPaint mTimePaint;
    private Drawable mPlayIconDrawable;
    private Paint.FontMetrics mTimeFontMetrics;
    private GestureDetector mGestureDetector;

    private boolean isTouching;
    private boolean isFling;

    private float mDividerHeight;
    @ColorInt
    private int mCurrentRowColor;
    @ColorInt
    private int mRowColor;
    @ColorInt
    private int mCenterRowColor;
    private String mDefaultLyric;
    @ColorInt
    private int mCenterLineMainColor;
    private int mScrollDuration;
    private int mTimeTextWidth;
    private int mPlayIconWidth;

    public LyricView(Context context) {
        this(context, null);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LyricView);
        float lyricTextSize = array.getDimension(R.styleable.LyricView_lyricTextSize, getResources().getDimension(R.dimen.lyricDefaultSize));
        float timeTextSize = array.getDimension(R.styleable.LyricView_timeTextSize, getResources().getDimension(R.dimen.timeDefaultSize));
        mDividerHeight = array.getDimension(R.styleable.LyricView_diverLineHeight, 100);
        mRowColor = array.getColor(R.styleable.LyricView_rowColor, Color.GRAY);
        mCurrentRowColor = array.getColor(R.styleable.LyricView_currentRowColor, Color.WHITE);
        mCenterRowColor = array.getColor(R.styleable.LyricView_centerRowColor, Color.WHITE);
        mDefaultLyric = array.getString(R.styleable.LyricView_defaultLyric);
        if (mDefaultLyric == null) {
            mDefaultLyric = getResources().getString(R.string.no_lyric);
        }
        mCenterLineMainColor = array.getColor(R.styleable.LyricView_centerLineMainColor, Color.GRAY);
        int centerLineHeight = (int) array.getDimension(R.styleable.LyricView_centerLineHeight, getResources().getDimension(R.dimen.centerLineDefaultHeight));
        mScrollDuration = array.getInteger(R.styleable.LyricView_scrollDuration, 1000);
        array.recycle();

        mLyricPaint = new TextPaint();
        mLyricPaint.setTextSize(lyricTextSize);
        mLyricPaint.setAntiAlias(true);

        mTimePaint = new TextPaint();
        mTimePaint.setTextSize(timeTextSize);
        mTimePaint.setAntiAlias(true);
        //noinspection SuspiciousNameCombination
        mTimePaint.setStrokeWidth(centerLineHeight);
        mTimePaint.setStrokeCap(Paint.Cap.ROUND);

        mTimeFontMetrics = mTimePaint.getFontMetrics();
        mTimeTextWidth = (int) getResources().getDimension(R.dimen.timeTextWidth);
        mPlayIconDrawable = getResources().getDrawable(R.drawable.ic_play_arrow_white_48dp);
        mPlayIconDrawable.setTint(mCenterLineMainColor);
        mPlayIconWidth = (int) getResources().getDimension(R.dimen.playIconWidth);
        mScroller = new Scroller(getContext());
        mGestureDetector = new GestureDetector(getContext(), mOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);
    }

    public void setLyricRows(List<LyricRow> rows) {
        if (mLyricRows == null) {
            mLyricRows = new ArrayList<>();
        }
        mLyricRows.clear();
        for (LyricRow row : rows) {
            mLyricRows.add(new WrapLyricRow(row, mLyricPaint, getWidth()));
        }
    }

    public void onPlayProgressChange(long milliseconds) {
        if (hasLyric()) {
            int row = findMatchLineByProgress(milliseconds);
            if (mCurrentRow != row) {
                mCurrentRow = row;
                if (!isCenterLineVisible) {
                    scrollToRow(mCurrentRow);
                } else {
                    invalidate();
                }
            }
        }
    }

    private boolean hasLyric() {
        return mLyricRows != null && !mLyricRows.isEmpty();
    }

    private float getOffsetByRow(int row) {
        if (mLyricRows.get(row).getOffset() == Float.MIN_VALUE) {
            float offset = getHeight() / 2;
            for (int i = 1; i <= row; i++) {
                offset -= (mLyricRows.get(i - 1).getRowHeight() + mLyricRows.get(i).getRowHeight()) / 2 + mDividerHeight;
            }
            mLyricRows.get(row).setOffset(offset);
        }
        return mLyricRows.get(row).getOffset();
    }

    private void alignCenter() {
        scrollToRow(getCenterRow(), 300);
    }

    private int findMatchLineByProgress(long milliseconds) {
        int lyricSize = mLyricRows.size();
        int left = 0;
        int right = lyricSize;
        while (left <= right) {
            int mid = (left + right) / 2;
            long midTime = mLyricRows.get(mid).getRow().getTimeIntValue();
            if (milliseconds < midTime) {
                right = mid - 1;
            } else {
                if (mid + 1 >= lyricSize || milliseconds < mLyricRows.get(mid + 1).getRow().getTimeIntValue()) {
                    return mid;
                }
                left = mid + 1;
            }
        }
        return 0;
    }

    private int getCenterRow() {
        int centerRow = 0;
        float minDistance = Float.MAX_VALUE;
        for (int i = 0; i < mLyricRows.size(); i++) {
            if (Math.abs(mOffset - getOffsetByRow(i)) < minDistance) {
                minDistance = Math.abs(mOffset - getOffsetByRow(i));
                centerRow = i;
            }
        }
        return centerRow;
    }

    private void scrollToRow(int row) {
        scrollToRow(row, mScrollDuration);
    }

    private void scrollToRow(int row, long duration) {
        if (mScrollAnimator != null && mScrollAnimator.isRunning()) {
            mScrollAnimator.end();
        }
        float offset = getOffsetByRow(row);
        mScrollAnimator = ValueAnimator.ofFloat(mOffset, offset);
        mScrollAnimator.setDuration(duration);
        mScrollAnimator.addUpdateListener(animation -> {
            mOffset = (float) animation.getAnimatedValue();
            invalidate();
        });
        mScrollAnimator.start();
    }

    /**
     * 画一行歌词
     *
     * @param y 歌词中心 Y 坐标
     */
    private void drawText(Canvas canvas, StaticLayout staticLayout, float y) {
        canvas.save();
        canvas.translate(0, y - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            // margin left
            int l = dp2px(2);
            // margin top
            int t = getHeight() / 2 - mPlayIconWidth / 2;
            int r = l + mPlayIconWidth;
            int b = t + mPlayIconWidth;
            mPlayIconDrawable.setBounds(l, t, r, b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;
        if (hasLyric()) {
            int centerRow = getCenterRow();
            if (isCenterLineVisible) {
                String time = mLyricRows.get(centerRow).getRow().getTime().split("\\.")[0];
                int timeStrLength = time.length();
                // 画播放图标
                mPlayIconDrawable.draw(canvas);
                // 画中心线
                mTimePaint.setColor(mCenterLineMainColor);
                canvas.drawLine(
                        mPlayIconWidth + dp2px(4),
                        centerY,
                        getWidth() - (mTimeTextWidth * timeStrLength / 2 + dp2px(2)),
                        centerY,
                        mTimePaint
                );
                // 画当前时间
                mTimePaint.setColor(mCenterLineMainColor);
                float timeX = getWidth() - mTimeTextWidth * timeStrLength / 2;
                float timeY = centerY - (mTimeFontMetrics.descent + mTimeFontMetrics.ascent) / 2;
                canvas.drawText(time, timeX, timeY, mTimePaint);
            }
            canvas.translate(0, mOffset);
            // 依次画出各行歌词
            float y = 0;
            for (int i = 0; i < mLyricRows.size(); i++) {
                if (i > 0) {
                    y += (mLyricRows.get(i - 1).getRowHeight() + mLyricRows.get(i).getRowHeight()) / 2 + mDividerHeight;
                }
                if (i == mCurrentRow) {
                    mLyricPaint.setColor(mCurrentRowColor);
                } else if (isCenterLineVisible && i == centerRow) {
                    mLyricPaint.setColor(mCenterRowColor);
                } else {
                    mLyricPaint.setColor(mRowColor);
                }
                drawText(canvas, mLyricRows.get(i).getStaticLayout(), y);
            }
        } else {
            mLyricPaint.setColor(mCurrentRowColor);
            @SuppressLint("DrawAllocation")
            StaticLayout staticLayout = new StaticLayout(
                    mDefaultLyric,
                    mLyricPaint,
                    getWidth(),
                    Layout.Alignment.ALIGN_CENTER,
                    1f,
                    0f,
                    false
            );
            drawText(canvas, staticLayout, centerY);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            isTouching = false;
            if (hasLyric() && !isFling) {
                alignCenter();
                postDelayed(mHideCenterLineTask, 3000);
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mHideCenterLineTask);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mOffset = mScroller.getCurrY();
            invalidate();
        }
        if (isFling && mScroller.isFinished()) {
            isFling = false;
            if (hasLyric() && !isTouching) {
                alignCenter();
                postDelayed(mHideCenterLineTask, 3000);
            }
        }
    }

    private Runnable mHideCenterLineTask = () -> {
        if (hasLyric() && isCenterLineVisible) {
            isCenterLineVisible = false;
            scrollToRow(mCurrentRow);
        }
    };

    private GestureDetector.SimpleOnGestureListener mOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return performClick();
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (hasLyric()) {
                mOffset += -distanceY;
                mOffset = Math.min(mOffset, getOffsetByRow(0));
                mOffset = Math.max(mOffset, getOffsetByRow(mLyricRows.size() - 1));
                invalidate();
                return true;
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (hasLyric()) {
                mScroller.fling(0, (int) mOffset, 0, (int) velocityY, 0, 0, (int) getOffsetByRow(mLyricRows.size() - 1), (int) getOffsetByRow(0));
                isFling = true;
                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (hasLyric()) {
                mScroller.forceFinished(true);
                removeCallbacks(mHideCenterLineTask);
                isCenterLineVisible = true;
                isTouching = true;
                invalidate();
                return true;
            }
            return super.onDown(e);
        }
    };

    public int dp2px(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
