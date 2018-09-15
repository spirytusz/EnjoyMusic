package com.zspirytus.enjoymusic.cache;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.zspirytus.enjoymusic.services.PlayMusicService;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

public class MyApplication extends Application {

    private static Context mApplicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = super.getApplicationContext();
        Intent intent = new Intent(this, PlayMusicService.class);
        startService(intent);
    }

    public static Context getGlobalContext() {
        return mApplicationContext;
    }
}
