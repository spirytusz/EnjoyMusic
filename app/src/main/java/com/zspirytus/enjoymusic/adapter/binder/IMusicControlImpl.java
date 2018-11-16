package com.zspirytus.enjoymusic.adapter.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class IMusicControlImpl extends IMusicControl.Stub {

    protected IMusicControlImpl() {
    }

    @Override
    public void play(Music music) throws RemoteException {
        MediaPlayController.getInstance().play(music);
    }

    @Override
    public void pause() throws RemoteException {
        MediaPlayController.getInstance().pause();
    }
}
