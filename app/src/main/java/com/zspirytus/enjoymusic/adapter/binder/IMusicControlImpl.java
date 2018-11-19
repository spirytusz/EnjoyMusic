package com.zspirytus.enjoymusic.adapter.binder;

import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class IMusicControlImpl extends IMusicControl.Stub {

    @Override
    public void play(Music music) {
        MediaPlayController.getInstance().play(music);
    }

    @Override
    public void pause() {
        MediaPlayController.getInstance().pause();
    }
}
