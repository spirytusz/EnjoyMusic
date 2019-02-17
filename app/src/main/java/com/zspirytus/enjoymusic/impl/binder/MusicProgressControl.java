package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class MusicProgressControl extends IMusicProgressControl.Stub {

    private static class SingletonHolder {
        static MusicProgressControl INSTANCE = new MusicProgressControl();
    }

    private MusicProgressControl() {
    }

    public static MusicProgressControl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void seekTo(int progress) {
        MediaPlayController.getInstance().seekTo(progress);
    }
}