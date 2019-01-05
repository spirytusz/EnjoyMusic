package com.zspirytus.enjoymusic.impl.binder;

import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class IMusicProgressControlImpl extends IMusicProgressControl.Stub {

    private static class SingletonHolder {
        static IMusicProgressControlImpl INSTANCE = new IMusicProgressControlImpl();
    }

    private IMusicProgressControlImpl() {
    }

    public static IMusicProgressControlImpl getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public void seekTo(int progress) {
        MediaPlayController.getInstance().seekTo(progress);
    }
}