package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.LyricRow;
import com.zspirytus.enjoymusic.services.LyricWidgetManager;

import java.util.List;

public class LyricWidget extends LinearLayout implements View.OnTouchListener {

    private static final long BACKGROUND_GONE_DELAY = 3000;
    private static final long CLICK_EVENT_DELAY = 100;
    private static final int X_THRESHOLD = 3;
    private static final int Y_THRESHOLD = 3;

    private List<LyricRow> mLyricRows;
    private Runnable mBgGoneTask = () -> {
        dismissBackground();
        LyricWidgetManager.getInstance().updateLyricWidget(LyricWidget.this, LyricWidget.this.lp);
    };
    private boolean isLock;
    private WindowManager.LayoutParams lp;

    private int x;
    private int y;
    private long touchDownTime;

    public LyricWidget(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_lyric, this, true);

        findViewById(R.id.lock).setOnClickListener(view -> {
            setLock(!isLock);
        });
        findViewById(R.id.close).setOnClickListener(view -> {
            LyricWidgetManager.getInstance().dismiss();
        });
        isLock = false;
        setOnTouchListener(this);
    }

    public void setLyricRows(List<LyricRow> rows) {
        mLyricRows = rows;
    }

    public void onProgressChange(long milliseconds) {
        int rowPos = findMatchLineByProgress(milliseconds);
        String lyric = mLyricRows.get(rowPos).getText();
        if (lyric != null && !lyric.isEmpty()) {
            setLyric(mLyricRows.get(rowPos).getText());
        }
    }

    public void setWindowManagerLayoutParams(WindowManager.LayoutParams lp) {
        this.lp = lp;
    }

    public void release() {
        lp = null;
        removeCallbacks(mBgGoneTask);
        mBgGoneTask = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                touchDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX - x;
                int movedY = nowY - y;
                if (!isLock && Math.abs(movedX) >= X_THRESHOLD) {
                    x = nowX;
                    lp.x = lp.x + movedX;
                }
                if (!isLock && Math.abs(movedY) >= Y_THRESHOLD) {
                    y = nowY;
                    lp.y = lp.y + movedY;
                }
                // 更新悬浮窗控件布局
                LyricWidgetManager.getInstance().updateLyricWidget(this, lp);
                break;
            case MotionEvent.ACTION_UP:
                long a = System.currentTimeMillis() - touchDownTime;
                if (a <= CLICK_EVENT_DELAY) {
                    showBackground();
                }
            default:
                break;
        }
        return false;
    }

    private void setLyric(String lyric) {
        ((TextView) findViewById(R.id.lyric)).setText(lyric);
    }

    private void setLock(boolean isLock) {
        this.isLock = isLock;
        int resId = isLock ? R.drawable.ic_lock_outline_black_24dp : R.drawable.ic_lock_open_black_24dp;
        ((ImageView) findViewById(R.id.lock)).setImageResource(resId);
        ((ImageView) findViewById(R.id.lock)).getDrawable().setTint(Color.WHITE);
        removeCallbacks(mBgGoneTask);
        postDelayed(mBgGoneTask, BACKGROUND_GONE_DELAY);
        LyricWidgetManager.getInstance().updateLyricWidget(this, lp);
    }

    private void showBackground() {
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        findViewById(R.id.bg).setVisibility(VISIBLE);
        LyricWidgetManager.getInstance().updateLyricWidget(this, lp);
        postDelayed(mBgGoneTask, BACKGROUND_GONE_DELAY);
    }

    private void dismissBackground() {
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        findViewById(R.id.bg).setVisibility(GONE);
        LyricWidgetManager.getInstance().updateLyricWidget(this, lp);
        removeCallbacks(mBgGoneTask);
    }

    private int findMatchLineByProgress(long milliseconds) {
        int lyricSize = mLyricRows.size();
        int left = 0;
        int right = lyricSize;
        while (left <= right) {
            int mid = (left + right) / 2;
            long midTime = mLyricRows.get(mid).getTimeIntValue();
            if (milliseconds < midTime) {
                right = mid - 1;
            } else {
                if (mid + 1 >= lyricSize || milliseconds < mLyricRows.get(mid + 1).getTimeIntValue()) {
                    return mid;
                }
                left = mid + 1;
            }
        }
        return 0;
    }
}
