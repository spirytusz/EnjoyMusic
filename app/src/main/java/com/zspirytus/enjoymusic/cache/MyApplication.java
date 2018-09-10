package com.zspirytus.enjoymusic.cache;

import android.app.Application;
import android.content.Context;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

public class MyApplication extends Application {

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = super.getApplicationContext();
    }

    public static Context getGlobalContext() {
        return mApplicationContext;
    }
}
