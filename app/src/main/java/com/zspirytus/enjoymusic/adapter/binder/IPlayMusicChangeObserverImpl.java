package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.ArrayList;
import java.util.List;

public class IPlayMusicChangeObserverImpl extends IPlayedMusicChangeObserver.Stub {

    private Music mEvent;

    private static class SingletonHolder {
        static IPlayMusicChangeObserverImpl INSTANCE = new IPlayMusicChangeObserverImpl();
    }

    private List<PlayedMusicChangeObserver> observers = new ArrayList<>();

    private IPlayMusicChangeObserverImpl() {
    }

    public static IPlayMusicChangeObserverImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void onPlayMusicChange(Music currentPlayingMusic) {
        mEvent = currentPlayingMusic;
        ForegroundMusicCache.getInstance().setCurrentPlayingMusic(currentPlayingMusic);
        for (PlayedMusicChangeObserver observer : observers) {
            observer.onPlayedMusicChanged(currentPlayingMusic);
        }
    }

    public void register(PlayedMusicChangeObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            if (mEvent != null)
                observer.onPlayedMusicChanged(mEvent);
        }
    }

    public void unregister(PlayedMusicChangeObserver observer) {
        observers.remove(observer);
    }
}
