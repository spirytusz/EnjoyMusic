package com.zspirytus.enjoymusic.services.media;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.listeners.observable.MusicStateObservable;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.io.IOException;

/**
 * 音乐播放暂停控制类
 * Created by ZSpirytus on 2018/8/10.
 */

public class MediaPlayController extends MusicStateObservable
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final int STATE_PLAYING = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_PREPARING = 4;

    private static MediaPlayer mediaPlayer;
    private static AudioManager audioManager;
    private static PlayingTimer mPlayingTimer;

    private int state;
    private boolean isPrepared = false;

    private static final MediaPlayController INSTANCE = new MediaPlayController();

    private MediaPlayController() {

        // init media obj
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(BaseActivity.getContext(), PowerManager.PARTIAL_WAKE_LOCK);
        audioManager = (AudioManager) BaseActivity.getContext().getSystemService(Service.AUDIO_SERVICE);

        // set listeners
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        // init timer
        mPlayingTimer = PlayingTimer.getInstance();
        mPlayingTimer.init(this);
    }

    public static MediaPlayController getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        beginPlay(mp);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic();
        play(nextMusic);
        notifyAllPlayedMusicChanged(nextMusic);
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
            if (currentPlayingMusic != null && currentPlayingMusic.equals(music)) {
                // selected music is currently playing or pausing or has not prepared
                if (isPrepared) {
                    // if has prepared, play it
                    if (!isPlaying()) {
                        beginPlay(mediaPlayer);
                        MusicCache.getInstance().setCurrentPlayingMusic(music);
                    }
                } else {
                    // else, prepare it
                    prepareMusic(music);
                }
            } else {
                // selected music is NOT currently playing or pausing
                notifyAllPlayedMusicChanged(music);
                prepareMusic(music);
                MusicCache.getInstance().setCurrentPlayingMusic(music);
                MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_BUFFERING);
                MyMediaSession.getInstance().setMetaData(music);
                MusicCache.getInstance().setCurrentPlayingMusic(music);
                state = STATE_PREPARING;
                isPrepared = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mPlayingTimer.pause();
            notifyAllMusicPlayingObserverPlayingState(false);
            state = STATE_PAUSE;
            MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_PAUSED);

        }
    }

    public void seekTo(int msec) {
        mediaPlayer.seekTo(msec);
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
        mediaPlayer.reset();
        mediaPlayer.setDataSource(music.getPath());
        mediaPlayer.prepareAsync();
    }

    private void beginPlay(MediaPlayer mp) {
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        notifyAllMusicPlayingObserverPlayingState(true);
        mp.start();
        MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_PLAYING);
        state = STATE_PLAYING;
        mPlayingTimer.start();
    }

}
