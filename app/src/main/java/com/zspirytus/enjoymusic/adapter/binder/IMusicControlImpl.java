package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class IMusicControlImpl extends IMusicControl.Stub {

    private static class SingletonHolder {
        static IMusicControlImpl INSTANCE = new IMusicControlImpl();
    }

    private IMusicControlImpl() {
    }

    public static IMusicControlImpl getInstance() {
        return SingletonHolder.INSTANCE;
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
