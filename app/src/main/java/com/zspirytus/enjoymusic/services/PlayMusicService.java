package com.zspirytus.enjoymusic.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.zspirytus.enjoymusic.model.Music;

public class PlayMusicService extends Service {

    private MyBinder binder = new MyBinder();

    public PlayMusicService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {

        private MediaPlayHelper mediaPlayHelper = MediaPlayHelper.getInstance();

        public void play(Music music) {
            NotificationHelper.showNotificationOnO(null,false);
            mediaPlayHelper.play(music);
        }

        public void pause() {
            NotificationHelper.showNotificationOnO(null, true);
            mediaPlayHelper.pause();
        }

        public void stop() {
            mediaPlayHelper.stop();
        }

    }

}
