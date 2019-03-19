package com.zspirytus.enjoymusic.engine;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.zspirytus.enjoymusic.global.MainApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = "CrashHandler";
    private static final String LOG_FILE_NAME = "crash.log";

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Map<String, String> infos;


    private CrashHandler() {
    }


    public static void init(Context context) {
        INSTANCE.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(INSTANCE);
        INSTANCE.collectDeviceInfo(context);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     *
     * @param thread 线程
     * @param ex     异常
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        ex.printStackTrace();
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param throwable 异常
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private void handleException(final Throwable throwable) {
        if (throwable == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                throwable.printStackTrace();
            }
        }.start();

        saveCrashInfo2File(throwable);
    }

    /**
     * 收集设备参数信息
     *
     * @param context 上下文
     */
    private void collectDeviceInfo(Context context) {
        infos = new HashMap<>();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常
     */
    private void saveCrashInfo2File(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        if (infos == null) {
            collectDeviceInfo(MainApplication.getAppContext());
        }
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        sb.append("\n").append("\n").append("\n");
        File logFile = new File(MainApplication.getAppContext().getCacheDir(), LOG_FILE_NAME);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (PrintWriter printWriter = new PrintWriter(logFile)) {
            printWriter.write(sb.toString());
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}