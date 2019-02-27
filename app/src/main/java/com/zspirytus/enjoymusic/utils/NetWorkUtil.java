package com.zspirytus.enjoymusic.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zspirytus.enjoymusic.global.MainApplication;

public class NetWorkUtil {

    private NetWorkUtil() {
        throw new AssertionError();
    }

    public static boolean isNetWorkReady() {
        ConnectivityManager cm =
                (ConnectivityManager) MainApplication.getForegroundContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
