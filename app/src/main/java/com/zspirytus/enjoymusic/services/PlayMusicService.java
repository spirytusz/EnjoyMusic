package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;

import org.simple.eventbus.EventBus;

/**
 * Service: 负责播放、暂停音乐
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends Service implements MusicPlayStateObserver {

    private MyBinder binder = new MyBinder();
    private MediaPlayController mMediaPlayController = MediaPlayController.getInstance();

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    public PlayMusicService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerEvent();
        MyMediaSession.getInstance().init(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvent();
        NotificationHelper.updateNotificationClearable(true);
    }

    @Override
    public void onPlayingState(boolean isPlaying) {

    }

    @Override
    public void onPlayCompleted() {
        // TODO: 2018/8/13 music loop or next or random play
    }

    private MediaBrowserCompat.MediaItem createMediaItem(MediaMetadataCompat metadata) {
        return new MediaBrowserCompat.MediaItem(
                metadata.getDescription(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        );
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);

        mMediaPlayController.register(this);

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

        mMediaPlayController.unregister(this);

        unregisterReceiver(myHeadSetPlugOutReceiver);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
        }
    }

    public class MyBinder extends Binder {

        private MediaPlayController mBinderMediaPlayController = MediaPlayController.getInstance();

        public void play(Music music) {
            NotificationHelper.showNotification(music);
            NotificationHelper.updateNotificationClearable(false);
            mBinderMediaPlayController.play(music);
        }

        public void pause() {
            NotificationHelper.updateNotificationClearable(true);
            mBinderMediaPlayController.pause();
        }

        public void stop() {
            mBinderMediaPlayController.stop();
        }

        public void seekTo(int msec) {
            mBinderMediaPlayController.seekTo(msec);
        }

    }

}
