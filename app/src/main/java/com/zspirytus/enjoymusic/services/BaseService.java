package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicPlayCompleteObserver;
import com.zspirytus.enjoymusic.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.IPlayStateChangeObserver;

public abstract class BaseService extends Service {

    private RemoteCallbackList<IPlayStateChangeObserver> mMusicPlayStateObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayProgressChangeObserver> mMusicPlayProgressObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IMusicPlayCompleteObserver> mMusicPlayCompleteObservers = new RemoteCallbackList<>();

    /**
     * register
     */
    public void registerPlayStateObserver(IPlayStateChangeObserver observer) {
        mMusicPlayStateObservers.register(observer);
    }

    public void registerProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mMusicPlayProgressObservers.register(observer);
    }

    public void registerMusicPlayCompleteObserver(IMusicPlayCompleteObserver observer) {
        mMusicPlayCompleteObservers.register(observer);
    }

    /**
     * unregister
     */
    public void unregisterPlayStateObserver(IPlayStateChangeObserver observer) {
        mMusicPlayStateObservers.unregister(observer);
    }

    public void unregisterProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mMusicPlayProgressObservers.unregister(observer);
    }

    public void unregisterMusicPlayCompleteObserver(IMusicPlayCompleteObserver observer) {
        mMusicPlayCompleteObservers.unregister(observer);
    }

    /**
     * notify
     */
    protected void notifyAllObserverPlayStateChange(boolean isPlaying) {
        int size = mMusicPlayStateObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mMusicPlayStateObservers.getBroadcastItem(i).onPlayStateChange(isPlaying);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mMusicPlayStateObservers.finishBroadcast();
    }

    protected void notifyAllObserverMusicPlayProgressChange(int currentPlayingMillis) {
        int size = mMusicPlayProgressObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mMusicPlayProgressObservers.getBroadcastItem(i).onProgressChange(currentPlayingMillis);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mMusicPlayProgressObservers.finishBroadcast();
    }

    protected int notifyAllObserversMusicPlayComplete() {
        int size = mMusicPlayCompleteObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mMusicPlayCompleteObservers.getBroadcastItem(i).onMusicPlayComplete();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mMusicPlayCompleteObservers.finishBroadcast();
        return size;
    }


}
