package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayedMusicChangeObserver;

public abstract class BaseService extends Service {

    private RemoteCallbackList<IPlayStateChangeObserver> mPlayStateObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayProgressChangeObserver> mPlayProgressObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayedMusicChangeObserver> mPlayMusicChangeObservers = new RemoteCallbackList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        registerEvent();
    }

    @Override
    public void onDestroy() {
        unregisterEvent();
        super.onDestroy();
    }

    protected void registerEvent() {
    }

    protected void unregisterEvent() {
    }

    /**
     * register
     */
    public void registerPlayStateObserver(IPlayStateChangeObserver observer) {
        mPlayStateObservers.register(observer);
    }

    public void registerProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mPlayProgressObservers.register(observer);
    }

    public void registerMusicPlayCompleteObserver(IPlayedMusicChangeObserver observer) {
        mPlayMusicChangeObservers.register(observer);
    }

    /**
     * unregister
     */
    public void unregisterPlayStateObserver(IPlayStateChangeObserver observer) {
        mPlayStateObservers.unregister(observer);
    }

    public void unregisterProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mPlayProgressObservers.unregister(observer);
    }

    public void unregisterMusicPlayCompleteObserver(IPlayedMusicChangeObserver observer) {
        mPlayMusicChangeObservers.unregister(observer);
    }

    /**
     * notify
     */
    protected void notifyAllObserverPlayStateChange(boolean isPlaying) {
        int size = mPlayStateObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mPlayStateObservers.getBroadcastItem(i).onPlayStateChange(isPlaying);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mPlayStateObservers.finishBroadcast();
    }

    protected void notifyAllObserverMusicPlayProgressChange(int currentPlayingMillis) {
        int size = mPlayProgressObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mPlayProgressObservers.getBroadcastItem(i).onProgressChange(currentPlayingMillis);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mPlayProgressObservers.finishBroadcast();
    }

    protected int notifyAllObserversMusicPlayComplete(Music currentPlayingMusic) {
        int size = mPlayMusicChangeObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mPlayMusicChangeObservers.getBroadcastItem(i).onPlayMusicChange(currentPlayingMusic);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mPlayMusicChangeObservers.finishBroadcast();
        return size;
    }


}
