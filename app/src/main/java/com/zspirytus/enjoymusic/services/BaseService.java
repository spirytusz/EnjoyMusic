package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IPlayMusicChangeObserver;
import com.zspirytus.enjoymusic.IPlayProgressChangeObserver;
import com.zspirytus.enjoymusic.IPlayStateChangeObserver;
import com.zspirytus.enjoymusic.entity.Music;

public abstract class BaseService extends Service {

    private RemoteCallbackList<IPlayStateChangeObserver> musicPlayStateObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayProgressChangeObserver> musicPlayProgressObservers = new RemoteCallbackList<>();
    private RemoteCallbackList<IPlayMusicChangeObserver> playedMusicChangeObservers = new RemoteCallbackList<>();

    private IPlayStateChangeObserver mIPlayStateChangeObserver;
    private IPlayProgressChangeObserver mIPlayProgressChangeObserver;
    private IPlayMusicChangeObserver mIPlayMusicChangeObserver;

    /**
     * register
     */
    public void registerPlayStateObserver(IPlayStateChangeObserver observer) {
        mIPlayStateChangeObserver = observer;
    }

    public void registerProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mIPlayProgressChangeObserver = observer;
    }

    public void registerPlayMusicChangeObserver(IPlayMusicChangeObserver observer) {
        mIPlayMusicChangeObserver = observer;
    }

    /**
     * unregister
     */
    public void unregisterPlayStateObserver(IPlayStateChangeObserver observer) {
        mIPlayStateChangeObserver = null;
    }

    public void unregisterProgressChangeObserver(IPlayProgressChangeObserver observer) {
        mIPlayProgressChangeObserver = null;
    }

    public void unregisterPlayMusicChangeObserver(IPlayMusicChangeObserver observer) {
        mIPlayMusicChangeObserver = null;
    }

    /**
     * notify
     */
    protected void notifyAllObserverPlayStateChange(boolean isPlaying) {
        try {
            mIPlayStateChangeObserver.onPlayStateChange(isPlaying);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void notifyAllObserverMusicPlayProgressChange(int currentPlayingMillis) {
        try {
            mIPlayProgressChangeObserver.onProgressChange(currentPlayingMillis);
        } catch (RemoteException e) {

        }
    }

    protected void notifyAllObserverPlayingMusicChanged(Music music) {
        try {
            mIPlayMusicChangeObserver.onMusicChange(music);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
