package com.zspirytus.enjoymusic.impl.binder;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IEqualizerHelper;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.services.media.EqualizerController;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

public class EqualizerHelper extends IEqualizerHelper.Stub {

    private static class Singleton {
        static EqualizerHelper INSTANCE = new EqualizerHelper();
    }

    private EqualizerHelper() {
    }

    public static EqualizerHelper getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public EqualizerMetaData addEqualizerSupport() throws RemoteException {
        return MediaPlayController.getInstance().addEqualizerSupport();
    }

    @Override
    public void setBandLevel(int band, int level) throws RemoteException {
        EqualizerController.setBandLevel((short) band, (short) level);
    }
}
