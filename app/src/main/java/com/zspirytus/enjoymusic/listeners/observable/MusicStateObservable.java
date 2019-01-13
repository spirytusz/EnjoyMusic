package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayedMusicChangeObserver;

public class MusicStateObservable {

    private RemoteCallbackList<IPlayStateChangeObserver> mPlayStateObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayProgressChangeObserver> mPlayProgressObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayedMusicChangeObserver> mPlayMusicChangeObservers = new RemoteCallbackList<>();

    /**
     * register
     */
    public synchronized void registerPlayStateObserver(IPlayStateChangeObserver observer) {
        mPlayStateObservers.register(observer);
    }

    public synchronized void registerProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mPlayProgressObservers.register(observer);
    }

    public synchronized void registerMusicPlayCompleteObserver(IPlayedMusicChangeObserver observer) {
        mPlayMusicChangeObservers.register(observer);
    }

    /**
     * unregister
     */
    public synchronized void unregisterPlayStateObserver(IPlayStateChangeObserver observer) {
        mPlayStateObservers.unregister(observer);
    }

    public synchronized void unregisterProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mPlayProgressObservers.unregister(observer);
    }

    public synchronized void unregisterMusicPlayCompleteObserver(IPlayedMusicChangeObserver observer) {
        mPlayMusicChangeObservers.unregister(observer);
    }

    /**
     * notify
     */
    protected synchronized void notifyAllObserverPlayStateChange(boolean isPlaying) {
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

    protected synchronized void notifyAllObserverMusicPlayProgressChange(int currentPlayingMillis) {
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

    protected synchronized void notifyAllObserverPlayMusicChange(Music currentPlayingMusic) {
        int size = mPlayMusicChangeObservers.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                mPlayMusicChangeObservers.getBroadcastItem(i).onPlayMusicChange(currentPlayingMusic);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mPlayMusicChangeObservers.finishBroadcast();
    }

}
