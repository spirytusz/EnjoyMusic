package com.zspirytus.enjoymusic.services;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public abstract class MyTimer {

    private Disposable mDisposable;
    private int period;

    public MyTimer(int period) {
        this.period = period;
    }

    public void start() {
        mDisposable = Observable.interval(period, TimeUnit.MILLISECONDS, Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(aLong -> {
                    onTime();
                });

    }

    public void pause() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    public abstract void onTime();
}
