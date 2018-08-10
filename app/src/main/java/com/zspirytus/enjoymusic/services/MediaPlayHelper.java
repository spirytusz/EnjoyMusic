package com.zspirytus.enjoymusic.services;

import android.media.MediaPlayer;

import com.zspirytus.enjoymusic.model.Music;

import java.io.IOException;

/**
 * Created by ZSpirytus on 2018/8/10.
 */

public class MediaPlayHelper implements MediaPlayer.OnPreparedListener {

    private static MediaPlayHelper INSTANCE = new MediaPlayHelper();
    private static MediaPlayer mediaPlayer = new MediaPlayer();

    private MediaPlayHelper() {
    }

    public static MediaPlayHelper getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    public void play(Music music) throws IOException {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        mediaPlayer.setDataSource(music.getPath());
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(this);
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
        }
    }


}
