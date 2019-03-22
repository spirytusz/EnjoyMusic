package com.zspirytus.basesdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.zspirytus.basesdk.thread.UIThreadSwitcher;

/**
 * Toast工具类
 * Created by ZSpirytus on 2018/8/3.
 */

public class ToastUtil {

    private static Toast toast;

    private ToastUtil() {
        throw new AssertionError("must not get class: " + this.getClass().getSimpleName() + " Instance!");
    }

    @SuppressLint("ShowToast")
    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

    @SuppressLint("ShowToast")
    public static void showToast(Context context, @StringRes int stringResId) {
        if (toast == null) {
            toast = Toast.makeText(context, stringResId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(stringResId);
        }
        toast.show();
    }

    public static void postToShow(final Context context, final String message) {
        UIThreadSwitcher.runOnMainThreadSync(new Runnable() {
            @Override
            public void run() {
                showToast(context, message);
            }
        });
    }

    public static void postToShow(final Context context, @StringRes final int stringResId) {
        UIThreadSwitcher.runOnMainThreadSync(new Runnable() {
            @Override
            public void run() {
                showToast(context, stringResId);
            }
        });
    }

}
