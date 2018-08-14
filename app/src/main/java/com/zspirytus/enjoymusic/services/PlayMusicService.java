package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;

import org.simple.eventbus.EventBus;

/**
 * Service: 负责播放、暂停音乐
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends Service implements MusicPlayStateObserver {

    private MyBinder binder = new MyBinder();
    private MediaPlayController mediaPlayController = MediaPlayController.getInstance();

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    public PlayMusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerEvent();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvent();
    }

    @Override
    public void onPlayingState(boolean isPlaying) {

    }

    @Override
    public void onPlayCompleted() {
        // TODO: 2018/8/13 music loop or next or random play
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);

        MediaPlayController.getInstance().register(this);

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

        MediaPlayController.getInstance().unregister(this);

        unregisterReceiver(myHeadSetPlugOutReceiver);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
        }
    }

    public class MyBinder extends Binder {

        private MediaPlayController mediaPlayController = MediaPlayController.getInstance();

        public void play(Music music) {
            NotificationHelper.showNotificationOnO(null, false);
            mediaPlayController.play(music);
        }

        public void pause() {
            NotificationHelper.showNotificationOnO(null, true);
            mediaPlayController.pause();
        }

        public void stop() {
            mediaPlayController.stop();
        }

    }

}
