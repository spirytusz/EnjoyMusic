package com.zspirytus.enjoymusic.impl.binder;

import android.net.Uri;

import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class MusicController extends IMusicControl.Stub {

    private static class SingletonHolder {
        static MusicController INSTANCE = new MusicController();
    }

    private MusicController() {
    }

    public static MusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void playUri(Uri uri) {
        Music music = MusicScanner.getInstance().getMusicByUri(uri);
        if (music != null) {
            play(music);
        } else {
            ToastUtil.postToShow(MainApplication.getAppContext(), R.string.invalid_file);
        }
    }

    @Override
    public void play(Music music) {
        MediaPlayController.getInstance().play(music);
    }

    @Override
    public void pause() {
        MediaPlayController.getInstance().pause();
    }

    @Override
    public void playNext(boolean fromUser) {
        Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic(fromUser);
        if (nextMusic != null) {
            play(nextMusic);
        }
    }

    @Override
    public void playPrevious() {
        Music previousMusic = MusicPlayOrderManager.getInstance().getPreviousMusic();
        if (previousMusic != null) {
            play(previousMusic);
        }
    }

    @Override
    public void setPlayMode(int playMode) {
        if (playMode < 4 && playMode >= 0) {
            MusicPlayOrderManager.getInstance().setPlayMode(playMode);
        }
    }
}
