package com.zspirytus.enjoymusic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.zspirytus.enjoymusic.engine.BackgroundMusicController;

/**
 * 耳机拔出事件接收广播
 * Created by ZSpirytus on 2018/8/11.
 */

public class MyHeadSetPlugOutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
            if (BackgroundMusicController.getInstance().isPlaying()) {
                BackgroundMusicController.getInstance().pause();
            }
        }
    }
}
