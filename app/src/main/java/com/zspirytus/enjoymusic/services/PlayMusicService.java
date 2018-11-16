package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.binder.BinderPoolImpl;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Service: 负责播放、暂停音乐、发送Notification相关事件
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends Service
        implements MusicPlayStateObserver, PlayedMusicChangeObserver {

    private BinderPoolImpl mBinderPool;
    private MediaPlayController mMediaPlayController = MediaPlayController.getInstance();

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        registerEvent();
        MyMediaSession.getInstance().init(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinderPool == null)
            mBinderPool = new BinderPoolImpl();
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvent();
        NotificationHelper.getInstance().updateNotificationClearable(true);
        CurrentPlayingMusicCache.getInstance().saveCurrentPlayingMusic();
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        NotificationHelper.getInstance().updateNotificationClearable(!isPlaying);
        NotificationHelper.getInstance().setPlayOrPauseBtnRes(isPlaying ? R.drawable.ic_pause_black_48dp : R.drawable.ic_play_arrow_black_48dp);
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        NotificationHelper.getInstance().showNotification(music);
    }

    @Subscriber(tag = Constant.EventBusTag.START_MAIN_ACTIVITY)
    public void startMainActivity(Object object) {
        MainActivity.startActivity(this, Constant.StatusBarEvent.EXTRA, Constant.StatusBarEvent.ACTION_NAME);
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().registerMusicPlayStateObserver(this);
        MediaPlayController.getInstance().registerPlayedMusicChangeObserver(this);

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
        MediaPlayController.getInstance().registerMusicPlayStateObserver(this);
        MediaPlayController.getInstance().unregisterPlayedMusicChangeObserver(this);

        unregisterReceiver(myHeadSetPlugOutReceiver);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
        }
    }
}
