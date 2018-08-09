package com.zspirytus.enjoymusic.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ZSpirytus on 2018/8/3.
 */

public class ToastUtil {

    private static Toast toast;

    private ToastUtil() {
        throw new AssertionError("must not get class: " + this.getClass().getSimpleName() + " Instance!");
    }

    public static void showToast(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }

}
