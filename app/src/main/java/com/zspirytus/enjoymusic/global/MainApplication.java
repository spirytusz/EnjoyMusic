package com.zspirytus.enjoymusic.global;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.engine.CrashHandler;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

public class MainApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * 两个进程都会初始化context.
         * 在不同的进程获得的context是不一样且非空的.
         * 如果想要获取当前进程的ApplicationContext,只需要MainApplication.getAppContext()即可获取.
         */
        mContext = this;
        CrashHandler.init(this);
        DBManager.getInstance().init(this);
    }

    public static Context getAppContext() {
        return mContext;
    }
}
