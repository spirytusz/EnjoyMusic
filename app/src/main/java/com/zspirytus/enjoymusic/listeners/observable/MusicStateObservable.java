package com.zspirytus.enjoymusic.listeners.observable;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayProgressObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/9.
 */

public class MusicStateObservable {

    private List<MusicPlayStateObserver> musicPlayStateObservers;
    private List<MusicPlayProgressObserver> musicPlayProgressObservers;
    private List<PlayedMusicChangeObserver> playedMusicChangeObservers;

    protected MusicStateObservable() {
        musicPlayStateObservers = new ArrayList<>();
        musicPlayProgressObservers = new ArrayList<>();
        playedMusicChangeObservers = new ArrayList<>();
    }

    // register MusicPlayStateObserver
    public void registerMusicPlayStateObserver(MusicPlayStateObserver musicPlayStateObserver) {
        if (!musicPlayStateObservers.contains(musicPlayStateObserver)) {
            musicPlayStateObservers.add(musicPlayStateObserver);
        }
    }

    // unregister MusicPlayStateObserver
    public void unregisterMusicPlayStateObserver(MusicPlayStateObserver musicPlayStateObserver) {
        musicPlayStateObservers.remove(musicPlayStateObserver);
    }

    // register MusicPlayProgressObserver
    public void registerProgressChange(MusicPlayProgressObserver musicPlayProgressObserver) {
        if (!musicPlayProgressObservers.contains(musicPlayProgressObserver)) {
            musicPlayProgressObservers.add(musicPlayProgressObserver);
        }
    }

    // unregister MusicPlayProgressObserver
    public void unregisterProgressChange(MusicPlayProgressObserver musicPlayProgressObserver) {
        musicPlayProgressObservers.remove(musicPlayProgressObserver);
    }

    // register PlayedMusicChangeObserver
    public void registerPlayedMusicChangeObserver(PlayedMusicChangeObserver playedMusicChangeObserver) {
        if (!playedMusicChangeObservers.contains(playedMusicChangeObserver)) {
            playedMusicChangeObservers.add(playedMusicChangeObserver);
        }
    }

    // unregister PlayedMusicChangeObserver
    public void unregisterPlayedMusicChangeObserver(PlayedMusicChangeObserver playedMusicChangeObserver) {
        playedMusicChangeObservers.remove(playedMusicChangeObserver);
    }

    // notify all MusicPlayStateObserver play state
    protected void notifyAllMusicPlayingObserverPlayingState(boolean isPlaying) {
        Iterator<MusicPlayStateObserver> observerIterator = musicPlayStateObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onPlayingStateChanged(isPlaying);
        }
    }

    // notify all MusicPlayProgressObserver playing progress
    public void notifyAllMusicPlayProgressChange(int currentPlayingMillis) {
        Iterator<MusicPlayProgressObserver> observerIterator = musicPlayProgressObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onProgressChanged(currentPlayingMillis);
        }
    }

    // notify all PlayedMusicChangeObserver played Music Changed
    protected void notifyAllPlayedMusicChanged(Music music) {
        Iterator<PlayedMusicChangeObserver> observerIterator = playedMusicChangeObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onPlayedMusicChanged(music);
        }
    }
}
