package com.zspirytus.enjoymusic.services;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.WindowManager;

import com.zspirytus.enjoymusic.engine.LyricLoader;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.view.widget.LyricWidget;

import java.io.File;

public class LyricWidgetManager {

    private LyricWidget lyricWidget;
    private WindowManager.LayoutParams lp;

    private String currentLyricFilePath = "";

    private static class Singleton {
        static LyricWidgetManager INSTANCE = new LyricWidgetManager();
    }

    private LyricWidgetManager() {
        lp = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        lp.format = PixelFormat.RGBA_8888;
        /*
         * 启动时默认不显示背景.
         */
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        lyricWidget = new LyricWidget(MainApplication.getAppContext());
        lyricWidget.setWindowManagerLayoutParams(lp);

        getWindowManager().addView(lyricWidget, lp);
    }

    public static LyricWidgetManager getInstance() {
        return Singleton.INSTANCE;
    }

    public void updateLyricWidget(LyricWidget lyricWidget, WindowManager.LayoutParams lp) {
        this.lyricWidget = lyricWidget;
        this.lp = lp;
        getWindowManager().updateViewLayout(lyricWidget, lp);
    }

    void onProgressChange(long milliseconds) {
        lyricWidget.onProgressChange(milliseconds);
        updateLyricWidget(lyricWidget, lp);
    }

    void loadLyric(String path) {
        if (path != null && !path.equals(currentLyricFilePath)) {
            lyricWidget.setLyricRows(LyricLoader.getInstance().load(new File(path)));
            currentLyricFilePath = path;
        } else {
            lyricWidget.setLyricRows(LyricLoader.getInstance().loadEmpty());
            currentLyricFilePath = "";
        }
    }

    public void dismiss() {
        lyricWidget.release();
        getWindowManager().removeViewImmediate(lyricWidget);
        lp = null;
        lyricWidget = null;
    }

    private WindowManager getWindowManager() {
        return (WindowManager) MainApplication.getAppContext().getSystemService(Context.WINDOW_SERVICE);
    }
}
