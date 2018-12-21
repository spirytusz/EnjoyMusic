package com.zspirytus.enjoymusic.global;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

/**
 * Created by ZSpirytus on 2018/9/10.
 */

public class MainApplication extends Application {

    private static final String MAIN_PROCESS_NAME = "com.zspirytus.enjoymusic";

    private static Context mForegroundContext;
    private static Context mBackgroundContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (MAIN_PROCESS_NAME.equals(getProcessNameByPid(this, android.os.Process.myPid()))) {
            mForegroundContext = getApplicationContext();
        } else {
            mBackgroundContext = getApplicationContext();
        }
    }

    private String getProcessNameByPid(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    public static Context getForegroundContext() {
        return mForegroundContext;
    }

    public static Context getBackgroundContext() {
        return mBackgroundContext;
    }
}
