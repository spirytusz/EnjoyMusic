package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;

import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Service: 负责播放、暂停音乐
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends Service {

    private MediaPlayController mMediaPlayController = MediaPlayController.getInstance();

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    private static Context mServiceContext;

    public PlayMusicService() {

    }

    public static Context getContext() {
        return mServiceContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerEvent();
        MyMediaSession.getInstance().init(this);
        mServiceContext = this.getApplicationContext();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvent();
        NotificationHelper.getInstance().updateNotificationClearable(true);
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);

        myHeadSetPlugOutReceiver = new MyHeadSetPlugOutReceiver();
        IntentFilter headsetPlugOutFilter = new IntentFilter();
        headsetPlugOutFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(myHeadSetPlugOutReceiver, headsetPlugOutFilter);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            myHeadSetButtonClickBelowLReceiver = new MyHeadSetButtonClickBelowLReceiver();
            IntentFilter headsetButtonClickFilter = new IntentFilter();
            headsetButtonClickFilter.addAction(Intent.ACTION_MEDIA_BUTTON);
            registerReceiver(myHeadSetButtonClickBelowLReceiver, headsetButtonClickFilter);
        }
    }

    private void unregisterEvent() {
        EventBus.getDefault().unregister(this);

        unregisterReceiver(myHeadSetPlugOutReceiver);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
        }
    }

    @Subscriber(tag = FinalValue.EventBusTag.PLAY)
    public void play(Music music) {
        NotificationHelper.getInstance().showNotification(music);
        NotificationHelper.getInstance().updateNotificationClearable(false);
        mMediaPlayController.play(music);
    }

    @Subscriber(tag = FinalValue.EventBusTag.PAUSE)
    public void pause(Music music) {
        NotificationHelper.getInstance().updateNotificationClearable(true);
        mMediaPlayController.pause();
    }

    @Subscriber(tag = FinalValue.EventBusTag.STOP)
    public void stop(Music music) {
        mMediaPlayController.stop();
    }

    @Subscriber(tag = FinalValue.EventBusTag.SEEK_TO)
    public void seekTo(int msec) {
        mMediaPlayController.seekTo(msec);
    }

    @Subscriber(tag = FinalValue.EventBusTag.START_MAIN_ACTIVITY)
    public void startMainActivity(Object object) {
        MainActivity.startActivity(this);
    }
}
