package com.zspirytus.enjoymusic.services;

import java.util.TimerTask;

public abstract class MyTimer {

    private java.util.Timer mTimer;
    private TimerTask mTimerTask;
    private boolean isTiming = false;

    private int period;

    public MyTimer(int period) {
        this.period = period;
    }

    public void start() {
        isTiming = true;
        mTimer = new java.util.Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
        mTimer.schedule(mTimerTask, 0, period);
    }

    public void pause() {
        if (isTiming()) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
            isTiming = false;
        }
    }

    public boolean isTiming() {
        return isTiming;
    }

    public abstract void onTime();
}
