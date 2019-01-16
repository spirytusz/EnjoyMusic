package com.zspirytus.enjoymusic.services.media;

import android.view.KeyEvent;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MediaButtonHandler {

    private static class SingletonHolder {
        static MediaButtonHandler INSTANCE = new MediaButtonHandler();
    }

    private static final int DELAY_MILLS = 600;

    private static final int STATE_IDLE = 0;
    private static final int STATE_LISTENING = 1;

    private OnHandleEvent mListener;
    private long[] cache;
    private int state;

    private MediaButtonHandler() {
        cache = new long[6];
    }

    public static MediaButtonHandler getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public MediaButtonHandler handleEvent(KeyEvent event) {
        int i = 0;
        while (i < cache.length && cache[i] > 0) {
            i++;
        }
        if (i == cache.length)
            return this;
        cache[i] = System.currentTimeMillis();
        if (state == STATE_IDLE) {
            AndroidSchedulers.mainThread().scheduleDirect(() -> {
                int clickNum = 0;
                while (clickNum < cache.length && cache[clickNum] != 0) {
                    clickNum++;
                }
                mListener.onHandleEvent(clickNum / 2);
                state = STATE_IDLE;
                cache = new long[6];
            }, DELAY_MILLS, TimeUnit.MILLISECONDS);
            state = STATE_LISTENING;
        }
        return this;
    }

    public void response(OnHandleEvent listener) {
        mListener = listener;
    }

    public interface OnHandleEvent {
        void onHandleEvent(int clickNum);
    }
}
