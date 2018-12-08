package com.zspirytus.enjoymusic.services;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;

import com.zspirytus.enjoymusic.BinderPool;
import com.zspirytus.enjoymusic.adapter.binder.IGetMusicListImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicProgressControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.ISetPlayListImpl;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.interfaces.RemotePlayMusicChangeCallback;
import com.zspirytus.enjoymusic.interfaces.RemotePlayProgressCallback;
import com.zspirytus.enjoymusic.interfaces.RemotePlayStateChangeCallback;
import com.zspirytus.enjoymusic.receivers.MyHeadSetButtonClickBelowLReceiver;
import com.zspirytus.enjoymusic.receivers.MyHeadSetPlugOutReceiver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.services.media.MyMediaSession;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Service: 负责播放、暂停音乐、发送Notification相关事件
 * Created by ZSpirytus on 2018/8/2.
 */

public class PlayMusicService extends BaseService implements RemotePlayProgressCallback,
        RemotePlayStateChangeCallback, RemotePlayMusicChangeCallback {

    private BinderPoolImpl mBinderPool;

    private MyHeadSetPlugOutReceiver myHeadSetPlugOutReceiver;
    private MyHeadSetButtonClickBelowLReceiver myHeadSetButtonClickBelowLReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
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
        //NotificationHelper.getInstance().updateNotificationClearable(true);
        //CurrentPlayingMusicCache.getInstance().saveCurrentPlayingMusic();
    }

    @Override
    public void onPlayStateChange(boolean isPlaying) {
        notifyAllObserverPlayStateChange(isPlaying);
    }

    @Override
    public void onProgressChange(int progress) {
        notifyAllObserverMusicPlayProgressChange(progress);
    }

    @Override
    public void onPlayMusicChange(Music currentPlayingMusic) {
        if (notifyAllObserversMusicPlayComplete(currentPlayingMusic) == 0) {
            // handle play music logic by service
        }
    }

    @Subscriber(tag = Constant.EventBusTag.START_MAIN_ACTIVITY)
    public void startMainActivity(Object object) {
        MainActivity.startActivity(this, Constant.StatusBarEvent.EXTRA, Constant.StatusBarEvent.ACTION_NAME);
    }

    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().setPlayProgressCallback(this);
        MediaPlayController.getInstance().setPlayStateChangeCallback(this);
        MediaPlayController.getInstance().setMusicPlayCompleteCallback(this);

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

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);

        unregisterReceiver(myHeadSetPlugOutReceiver);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterReceiver(myHeadSetButtonClickBelowLReceiver);
        }
    }

    private class BinderPoolImpl extends BinderPool.Stub {

        @Override
        public IBinder queryBinder(int binderCode) {
            switch (binderCode) {
                case Constant.BinderCode.MUSIC_CONTROL:
                    return IMusicControlImpl.getInstance();
                case Constant.BinderCode.MUSIC_PROGRESS_CONTROL:
                    return IMusicProgressControlImpl.getInstance();
                case Constant.BinderCode.GET_MUSIC_LIST:
                    return IGetMusicListImpl.getInstance();
                case Constant.BinderCode.SET_PLAY_LIST:
                    return ISetPlayListImpl.getInstance();
                default:
                    return null;
            }
        }

        @Override
        public void registerObserver(IBinder observer, int binderCode) {
            switch (binderCode) {
                case Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER:
                    registerPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER:
                    registerProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER:
                    registerMusicPlayCompleteObserver(IPlayedMusicChangeObserver.Stub.asInterface(observer));
                    break;
            }
        }

        @Override
        public void unregisterObserver(IBinder observer, int binderCode) {
            switch (binderCode) {
                case Constant.BinderCode.PLAY_STATE_CHANGE_OBSERVER:
                    unregisterPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_PROGRESS_CHANGE_OBSERVER:
                    unregisterProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderCode.PLAY_MUSIC_CHANGE_OBSERVER:
                    unregisterMusicPlayCompleteObserver(IPlayedMusicChangeObserver.Stub.asInterface(observer));
                    break;
            }
        }
    }
}
