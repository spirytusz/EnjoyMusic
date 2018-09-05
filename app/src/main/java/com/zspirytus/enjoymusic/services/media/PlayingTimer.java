package com.zspirytus.enjoymusic.services.media;

import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ZSpirytus on 2018/9/4.
 */

public class PlayingTimer {

    private static PlayingTimer INSTANCE;

    private static Timer mTimer;
    private static TimerTask mTimerTask;
    private MediaPlayController mMediaPlayController;

    private PlayingTimer() {

    }

    public static PlayingTimer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayingTimer();
        }
        return INSTANCE;
    }

    public void init(MediaPlayController mediaPlayController) {
        mMediaPlayController = mediaPlayController;
    }

    public void start() {
        final int SECONDS = 1000;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                BaseActivity.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int currentPlayingMillis = mMediaPlayController.getCurrentPosition();
                        mMediaPlayController.notifyAllMusicPlayProgressChange(currentPlayingMillis);
                    }
                });
            }
        };
        mTimer.schedule(mTimerTask, 0, SECONDS);
    }

    public void pause() {
        mTimer.cancel();
        mTimer = null;
        mTimerTask.cancel();
        mTimerTask = null;
    }

    public void stop() {
        pause();
    }

}
