package com.zspirytus.enjoymusic.services.media;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZSpirytus on 2018/9/4.
 */

public class PlayingTimer {

    private static class SingletonHolder {
        private static PlayingTimer INSTANCE = new PlayingTimer();
    }

    private static Timer mTimer;
    private static TimerTask mTimerTask;
    private MediaPlayController mMediaPlayController;

    private boolean hasStart = false;

    private PlayingTimer() {
    }

    public static PlayingTimer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(MediaPlayController mediaPlayController) {
        mMediaPlayController = mediaPlayController;
    }

    public void start() {
        hasStart = true;
        final int SECONDS = 1000;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                int currentPlayingSeconds = mMediaPlayController.getCurrentPosition() / 1000;
                mMediaPlayController.notifyAllMusicPlayProgressChange(currentPlayingSeconds);
            }
        };
        mTimer.schedule(mTimerTask, 0, SECONDS);
    }

    public void pause() {
        if (hasStart) {
            mTimer.cancel();
            mTimer = null;
            mTimerTask.cancel();
            mTimerTask = null;
            hasStart = false;
        }
    }

    public void stop() {
        pause();
    }

}
