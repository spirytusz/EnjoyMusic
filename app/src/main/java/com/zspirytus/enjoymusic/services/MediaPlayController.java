package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 音乐播放暂停控制类
 * Created by ZSpirytus on 2018/8/10.
 */

public class MediaPlayController
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final int STATE_NOT_INIT = 0;
    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_PREPARING = 4;

    private static MediaPlayer mediaPlayer;
    private static List<MusicPlayStateObserver> musicPlayStateObservers;

    private static AudioManager audioManager;
    private int state;

    private static MediaPlayController INSTANCE = new MediaPlayController();

    private MediaPlayController() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(BaseActivity.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        audioManager = (AudioManager) BaseActivity.getContext().getSystemService(Service.AUDIO_SERVICE);
        musicPlayStateObservers = new ArrayList<>();
    }

    public static MediaPlayController getInstance() {
        return INSTANCE;
    }

    public boolean register(MusicPlayStateObserver MusicPlayStateObserver) {
        if (!musicPlayStateObservers.contains(MusicPlayStateObserver)) {
            musicPlayStateObservers.add(MusicPlayStateObserver);
            return true;
        }
        return false;
    }

    public boolean unregister(MusicPlayStateObserver MusicPlayStateObserver) {
        musicPlayStateObservers.remove(MusicPlayStateObserver);
        return true;
    }

    private void notifyAllMusicPlayingObserverPlayingState(boolean isPlaying) {
        Iterator<MusicPlayStateObserver> observerIterator = musicPlayStateObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onPlayingState(isPlaying);
        }
    }

    private void notifyAllMusicPlayingObserverPlayCompleted() {
        Iterator<MusicPlayStateObserver> observerIterator = musicPlayStateObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onPlayCompleted();
        }
    }

    public static boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        beginPlay(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        audioManager.abandonAudioFocus(this);
        notifyAllMusicPlayingObserverPlayCompleted();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public void play(Music music) {
        try {
            Music currentPlayingMusic = MusicCache.getInstance().getCurrentPlayingMusic();
            if (currentPlayingMusic != null && currentPlayingMusic.equals(music) && state > STATE_NOT_INIT) {
                if (!isPlaying()) {
                    beginPlay(mediaPlayer);
                    MusicCache.getInstance().setCurrentPlayingMusic(music);
                }
            } else {
                if (state == STATE_NOT_INIT) {
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                }
                mediaPlayer.reset();
                prepareMusic(music);
                MusicCache.getInstance().setCurrentPlayingMusic(music);
                state = STATE_PREPARING;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            notifyAllMusicPlayingObserverPlayingState(false);
            state = STATE_PAUSE;
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    private void prepareMusic(Music music) throws IOException {
        mediaPlayer.setDataSource(music.getPath());
        mediaPlayer.prepareAsync();
    }

    private void beginPlay(MediaPlayer mp) {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        notifyAllMusicPlayingObserverPlayingState(true);
        mp.start();
        state = STATE_PLAYING;
    }

}
