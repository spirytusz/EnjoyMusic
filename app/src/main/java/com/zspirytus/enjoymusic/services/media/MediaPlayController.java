package com.zspirytus.enjoymusic.services.media;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.MusicPlayProgressObserver;
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
    private static AudioManager audioManager;
    private static List<MusicPlayStateObserver> musicPlayStateObservers;
    private static List<MusicPlayProgressObserver> musicPlayProgressObservers;

    private int state;

    private static final MediaPlayController INSTANCE = new MediaPlayController();

    private MediaPlayController() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(BaseActivity.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        audioManager = (AudioManager) BaseActivity.getContext().getSystemService(Service.AUDIO_SERVICE);
        musicPlayStateObservers = new ArrayList<>();
        musicPlayProgressObservers = new ArrayList<>();
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

    public boolean registerProgressChange(MusicPlayProgressObserver musicPlayProgressObserver) {
        if (!musicPlayProgressObservers.contains(musicPlayProgressObserver)) {
            musicPlayProgressObservers.add(musicPlayProgressObserver);
            return true;
        }
        return false;
    }

    public boolean unregisterProgressChange(MusicPlayProgressObserver musicPlayProgressObserver) {
        musicPlayProgressObservers.remove(musicPlayProgressObserver);
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

    private void notifyAllMusicPlayProgressChange(int progress) {
        Iterator<MusicPlayProgressObserver> observerIterator = musicPlayProgressObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onProgressChange(progress);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        beginPlay(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        audioManager.abandonAudioFocus(this);
        notifyAllMusicPlayingObserverPlayCompleted();
        play(MusicCache.getInstance().getCurrentPlayingMusic());
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // pause
                break;
            case AudioManager.AUDIOFOCUS_GAIN:
                // play
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                //stop
                break;
        }
    }

    public boolean isPlaying() {
        return state == STATE_PLAYING;
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
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
                MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_BUFFERING);
                state = STATE_PREPARING;
            }
            MyMediaSession.getInstance().setMetaData(music);
            MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_PLAYING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            notifyAllMusicPlayingObserverPlayingState(false);
            state = STATE_PAUSE;
            MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    public void seekTo(int progress) {
        int position = (int) ((MusicCache.getInstance().getCurrentPlayingMusic().getDuration() * progress) / 100);
        mediaPlayer.seekTo(position);
    }

    public void playNext() {
        play(MusicCache.getInstance().getNextMusic(MusicCache.MODE_ORDER));
    }

    public void playPrevious() {
        play(MusicCache.getInstance().getPreviousMusic(MusicCache.MODE_ORDER));
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_STOPPED);
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
