package com.zspirytus.enjoymusic.services;

import android.app.Service;

public abstract class BaseService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        registerEvent();
    }

    @Override
    public void onDestroy() {
        unregisterEvent();
        super.onDestroy();
    }

    protected void registerEvent() {
    }

    protected void unregisterEvent() {
    }

}
