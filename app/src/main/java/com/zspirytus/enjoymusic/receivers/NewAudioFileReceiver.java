package com.zspirytus.enjoymusic.receivers;

import android.database.ContentObserver;
import android.os.Handler;
import android.provider.MediaStore;

import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.global.MainApplication;

public abstract class NewAudioFileReceiver {

    private static final String TAG = "NewAudioFileReceiver";

    public NewAudioFileReceiver() {
        MainApplication.getAppContext().getContentResolver().registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        onNewMusicAdd(MusicScanner.getInstance().scanAddedMusic());
                    }
                }
        );
    }

    public abstract void onNewMusicAdd(Music music);
}
