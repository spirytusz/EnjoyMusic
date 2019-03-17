package com.zspirytus.enjoymusic.utils;

import android.util.Log;

import com.zspirytus.enjoymusic.global.MainApplication;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

/**
 * 日志工具类
 * Created by ZSpirytus on 2018/8/11.
 */

public class LogUtil {

    private LogUtil() {
        throw new AssertionError("must not get class: " + this.getClass().getSimpleName() + " Instance!");
    }

    private static final int DEBUG = 1;
    private static final int RELEASE = 2;
    private static final int LEVEL = DEBUG;

    public static void e(String TAG, String message) {
        if (LEVEL == DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static void out(Object message) {
        if (LEVEL == DEBUG) {
            System.out.println(message);
        }
    }

    public static void err(Object message) {
        if (LEVEL == DEBUG) {
            System.err.println(message);
        }
    }

    public static void log(String fileName, String msg) {
        PrintStream stream;
        try {
            File file = createLogFile(fileName);
            stream = new PrintStream(file);
            stream.append(msg);
            stream.flush();
            stream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void log(String fileName, Throwable e) {
        PrintStream stream;
        try {
            File file = createLogFile(fileName);
            stream = new PrintStream(file);
            e.printStackTrace(stream);
            stream.flush();
            stream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File createLogFile(String fileName) throws IOException {
        File dataFile = MainApplication.getAppContext().getExternalCacheDir();
        File logFileDir = new File(dataFile, "log");
        logFileDir.mkdir();
        File logFile = new File(logFileDir, fileName);
        logFile.createNewFile();
        return logFile;
    }

}
