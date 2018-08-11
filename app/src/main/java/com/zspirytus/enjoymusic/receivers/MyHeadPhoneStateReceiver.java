package com.zspirytus.enjoymusic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zspirytus.enjoymusic.services.MediaPlayHelper;

/**
 * Created by ZSpirytus  on 2018/8/11.
 */

public class MyHeadPhoneStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            if (intent.hasExtra("state")) {
                int state = intent.getIntExtra("state", 0);
                MediaPlayHelper mediaPlayHelper = MediaPlayHelper.getInstance();
                if (state == 1) {
                    // 插入耳机
                } else {
                    // 拔出耳机
                    mediaPlayHelper.interruptByEvent();
                }
            }
        }
    }
}
