package com.zspirytus.enjoymusic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.BackgroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.services.NotificationHelper;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import org.simple.eventbus.EventBus;

import java.lang.reflect.Method;

/**
 * Created by ZSpirytus on 2018/9/8.
 */

public class StatusBarEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String event = intent.getStringExtra(Constant.StatusBarEvent.EXTRA);
        if (event != null) {
            switch (event) {
                case Constant.StatusBarEvent.SINGLE_CLICK:
                    EventBus.getDefault().post(Constant.StatusBarEvent.SINGLE_CLICK, Constant.EventBusTag.START_MAIN_ACTIVITY);
                    collapseStatusBar(MyApplication.getGlobalContext());
                    break;
                case Constant.StatusBarEvent.PREVIOUS:
                    BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getPreviousMusic());
                    break;
                case Constant.StatusBarEvent.PLAY_OR_PAUSE:
                    if (MediaPlayController.getInstance().isPlaying()) {
                        BackgroundMusicController.getInstance().pause(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
                        NotificationHelper.getInstance().setPlayOrPauseBtnRes(R.drawable.ic_play_arrow_black_48dp);
                    } else {
                        BackgroundMusicController.getInstance().play(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
                        NotificationHelper.getInstance().setPlayOrPauseBtnRes(R.drawable.ic_pause_black_48dp);
                    }
                    break;
                case Constant.StatusBarEvent.NEXT:
                    BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic());
                    break;
            }
        }
    }

    private void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            collapse = statusBarManager.getClass().getMethod("collapsePanels");
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

}
