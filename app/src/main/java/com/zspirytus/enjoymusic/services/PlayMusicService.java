package com.zspirytus.enjoymusic.services;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.BinderPool;
import com.zspirytus.enjoymusic.IPlayMusicChangeObserver;
import com.zspirytus.enjoymusic.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.adapter.binder.IMusicControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicProgressControlImpl;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
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

public class PlayMusicService extends BaseService implements RemotePlayMusicChangeCallback,
        RemotePlayProgressCallback, RemotePlayStateChangeCallback {

    private BinderPoolImpl mBinderPool;

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
    public void onMusicChange(Music music) {
        notifyAllObserverPlayingMusicChanged(music);
    }

    @Override
    public void onPlayStateChange(boolean isPlaying) {
        notifyAllObserverPlayStateChange(isPlaying);
    }

    @Override
    public void onProgressChange(int progress) {
        notifyAllObserverMusicPlayProgressChange(progress);
    }

    @Subscriber(tag = Constant.EventBusTag.START_MAIN_ACTIVITY)
    public void startMainActivity(Object object) {
        MainActivity.startActivity(this, Constant.StatusBarEvent.EXTRA, Constant.StatusBarEvent.ACTION_NAME);
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().setPlayMusicChangeCallback(this);
        MediaPlayController.getInstance().setPlayProgressCallback(this);
        MediaPlayController.getInstance().setPlayStateChangeCallback(this);

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

    private class BinderPoolImpl extends BinderPool.Stub {

        private IMusicControlImpl mIMusicControlImpl;
        private IMusicProgressControlImpl mIMusicProgressControlImpl;

        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            switch (binderCode) {
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_CONTROL:
                    if (mIMusicControlImpl == null) {
                        mIMusicControlImpl = new IMusicControlImpl();
                    }
                    return mIMusicControlImpl;
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_PROGRESS_CONTROL:
                    if (mIMusicProgressControlImpl == null) {
                        mIMusicProgressControlImpl = new IMusicProgressControlImpl();
                    }
                    return mIMusicProgressControlImpl;
                default:
                    return null;
            }
        }

        @Override
        public void registerObserver(IBinder observer, int binderCode) throws RemoteException {
            switch (binderCode) {
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_PLAY_STATE_CHANGE_OBSERVER:
                    registerPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_CHANGE_OBSERVER:
                    registerPlayMusicChangeObserver(IPlayMusicChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_PROGRESS_OBSERVER:
                    registerProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                    break;
            }
        }

        @Override
        public void unregisterObserver(IBinder observer, int binderCode) throws RemoteException {
            switch (binderCode) {
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_PLAY_STATE_CHANGE_OBSERVER:
                    unregisterPlayStateObserver(IPlayStateChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_CHANGE_OBSERVER:
                    unregisterPlayMusicChangeObserver(IPlayMusicChangeObserver.Stub.asInterface(observer));
                    break;
                case Constant.BinderPoolCode.BINDER_POOL_MUSIC_PROGRESS_OBSERVER:
                    unregisterProgressChangeObserver(IPlayProgressChangeObserver.Stub.asInterface(observer));
                    break;
            }
        }
    }
}
