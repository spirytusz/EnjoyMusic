package com.zspirytus.enjoymusic.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;

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

    public static void postToShow(Context context, String message) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> showToast(context, message));
    }

    public static void postToShow(Context context, @StringRes int stringResId) {
        AndroidSchedulers.mainThread().scheduleDirect(() -> showToast(context, stringResId));
    }

}
