package com.zspirytus.enjoymusic.adapter.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class IMusicProgressControlImpl extends IMusicProgressControl.Stub {
    @Override
    public void seekTo(int progress) throws RemoteException {
        MediaPlayController.getInstance().seekTo(progress);
    }
}