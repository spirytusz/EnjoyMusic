package com.zspirytus.enjoymusic.services.media;

import android.app.Service;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.annotation.BinderThread;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zspirytus.enjoymusic.cache.BackgroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.engine.PlayHistoryManager;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.listeners.IOnRemotePauseListener;
import com.zspirytus.enjoymusic.listeners.IOnRemotePlayListener;
import com.zspirytus.enjoymusic.listeners.observable.MusicStateObservable;
import com.zspirytus.enjoymusic.services.MyTimer;
import com.zspirytus.enjoymusic.services.NotificationHelper;

import java.io.IOException;

/**
 * 音乐播放暂停控制类
 * Created by ZSpirytus on 2018/8/10.
 */

public class MediaPlayController extends MusicStateObservable
        implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
        AudioManager.OnAudioFocusChangeListener {

    private static final String TAG = "MediaPlayController";

    private static final MediaPlayController INSTANCE = new MediaPlayController();
    private static final int STATE_IDLE = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_PREPARED = 3;
    private static final int STATE_STARTED = 4;
    private static final int STATE_PAUSED = 5;
    private static final int STATE_PLAYBACK_COMPLETED = 6;
    private static final int STATE_STOP = 7;

    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private MyTimer mPlayingTimer;
    private int state;
    private IOnRemotePlayListener mOnPlayListener;
    private IOnRemotePauseListener mOnPauseListener;

    private MediaPlayController() {
        // init MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(MainApplication.getAppContext(), PowerManager.PARTIAL_WAKE_LOCK);
        audioManager = (AudioManager) MainApplication.getAppContext().getSystemService(Service.AUDIO_SERVICE);
        // set listeners
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        // init timer
        mPlayingTimer = new MyTimer(300) {
            @Override
            public void onTime() {
                long milliseconds = getCurrentPosition();
                notifyAllObserverMusicPlayProgressChange((int) milliseconds);
                notifyAllRemoteObserverPlayProgresChange(milliseconds);
            }
        };
        mPlayingTimer.start();
        // set MediaPlayer State
        setState(STATE_IDLE);
    }

    public int getAudioSessionId() {
        if (mediaPlayer != null) {
            return mediaPlayer.getAudioSessionId();
        } else {
            return 0;
        }
    }

    public void setOnPlayListener(IOnRemotePlayListener listener) {
        mOnPlayListener = listener;
    }

    public void setOnPauseListener(IOnRemotePauseListener listener) {
        mOnPauseListener = listener;
    }

    public static MediaPlayController getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setState(STATE_PLAYBACK_COMPLETED);
        Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic(false);
        if (nextMusic != null) {
            ThreadPool.execute(() -> play(nextMusic));
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
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
        return state == STATE_STARTED;
    }

    @BinderThread
    private void setState(int state) {
        this.state = state;
    }

    int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @BinderThread
    public synchronized void play(Music music) {
        try {
            Music currentMusic = BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic();
            /*
             * 如果和当前播放的音乐不同，或者处于stop状态，则reset并prepare
             * 否则继续播放
             */
            if (currentMusic == null || !music.equals(currentMusic) || state == STATE_STOP || state == STATE_IDLE) {
                if (!VisualizerHelper.isEnable()) {
                    VisualizerHelper.setEnable(true);
                }
                reset();
                prepareMusic(music);
            }
            beginPlay(music);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BinderThread
    public synchronized void pause() {
        if (state == STATE_STARTED) {
            VisualizerHelper.setEnable(false);
            mediaPlayer.pause();
            setState(STATE_PAUSED);
            BackgroundMusicStateCache.getInstance().setPlaying(false);
            mPlayingTimer.pause();
            if (mOnPauseListener != null) {
                mOnPauseListener.onPause();
            }
            NotificationHelper.getInstance().updateNotification(false);
            notifyAllObserverPlayStateChange(false);
            MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    @BinderThread
    public void seekTo(int milliseconds) {
        mediaPlayer.seekTo(milliseconds);
    }

    @BinderThread
    private void reset() {
        mediaPlayer.reset();
    }

    /**
     * 直接Prepare Music. 因为prepareMusic运行在Binder线程池中的线程
     * 所以不用异步prepare.
     *
     * @param music 需要prepare的music
     * @throws IOException
     */
    @BinderThread
    private void prepareMusic(Music music) throws IOException {
        mediaPlayer.setDataSource(music.getMusicFilePath());
        setState(STATE_INITIALIZED);
        mediaPlayer.prepare();
        setState(STATE_PREPARED);
    }

    @BinderThread
    private void beginPlay(Music music) {
        Music currentPlayingMusic = BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic();
        if (currentPlayingMusic == null || !currentPlayingMusic.equals(music)) {
            notifyAllObserverPlayMusicChange(music);
            currentPlayingMusic = music;
            BackgroundMusicStateCache.getInstance().setCurrentPlayingMusic(music);
        }
        MyMediaSession.getInstance().setMetaData(currentPlayingMusic);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        mediaPlayer.start();
        setState(STATE_STARTED);
        BackgroundMusicStateCache.getInstance().setPlaying(true);
        NotificationHelper.getInstance().showNotification(currentPlayingMusic);
        NotificationHelper.getInstance().updateNotification(true);
        if (mOnPlayListener != null) {
            mOnPlayListener.onPlay(music);
        }
        mPlayingTimer.start();
        notifyAllObserverPlayStateChange(true);
        MyMediaSession.getInstance().setPlaybackState(PlaybackStateCompat.STATE_PLAYING);
        PlayHistoryManager.getInstance().add(currentPlayingMusic);
    }

    public void release() {
        mediaPlayer.stop();
        setState(STATE_STOP);
        VisualizerHelper.setEnable(false);
    }
}
