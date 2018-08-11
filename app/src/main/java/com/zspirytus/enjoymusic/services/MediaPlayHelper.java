package com.zspirytus.enjoymusic.services;

import android.media.MediaPlayer;
import android.os.CountDownTimer;

import com.zspirytus.enjoymusic.model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/8/10.
 */

public class MediaPlayHelper implements MediaPlayer.OnPreparedListener {

    private static final String TAG = "MediaPlayHelper";

    private static MediaPlayHelper INSTANCE = new MediaPlayHelper();
    private static MediaPlayer mediaPlayer = new MediaPlayer();
    private static List<MusicPlayingObserver> musicPlayingObservers = new ArrayList<>();

    private boolean isBlockByPhone = false;

    private Music currentPlayingMusic;

    private MediaPlayHelper() {

    }

    public static MediaPlayHelper getInstance() {
        return INSTANCE;
    }

    public static boolean register(MusicPlayingObserver musicPlayingObserver) {
        if (!musicPlayingObservers.contains(musicPlayingObserver)) {
            musicPlayingObservers.add(musicPlayingObserver);
            return true;
        }
        return false;
    }

    public static boolean unregister(MusicPlayingObserver musicPlayingObserver) {
        musicPlayingObservers.remove(musicPlayingObserver);
        return true;
    }

    public static boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        notifyAllMusicPlayingObserver(true);
        mp.start();
        mp.setLooping(true);
    }

    public void play(Music music) {
        try {
            if (currentPlayingMusic != null) {
                if (!currentPlayingMusic.equals(music)) {
                    mediaPlayer.reset();
                    prepareMusic(music);
                    currentPlayingMusic = music;
                } else if (!mediaPlayer.isPlaying()) {
                    notifyAllMusicPlayingObserver(true);
                    mediaPlayer.start();
                }
            } else {
                prepareMusic(music);
                currentPlayingMusic = music;
            }
            mediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            notifyAllMusicPlayingObserver(false);
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }

    public void interruptByEvent() {
        pause();
    }

    public void resumeAfterEvent() {
        play(currentPlayingMusic);
    }

    private void prepareMusic(Music music) throws IOException {
        mediaPlayer.setDataSource(music.getPath());
        mediaPlayer.prepareAsync();
    }

    private void notifyAllMusicPlayingObserver(boolean isPlaying) {
        Iterator<MusicPlayingObserver> observerIterator = musicPlayingObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().update(isPlaying);
        }
    }

}
