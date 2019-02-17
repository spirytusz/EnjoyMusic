package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IEqualizerHelper;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;

public class ForegroundEqualizer {

    private IEqualizerHelper mEqualizerHelper;

    private static class Singleton {
        static ForegroundEqualizer INSTANCE = new ForegroundEqualizer();
    }

    private ForegroundEqualizer() {
    }

    public static ForegroundEqualizer getInstance() {
        return Singleton.INSTANCE;
    }

    public void setBandLevel(short band, short level) {
        ThreadPool.execute(() -> {
            if (mEqualizerHelper == null) {
                IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.EQUALIZER_HELPER);
                mEqualizerHelper = IEqualizerHelper.Stub.asInterface(binder);
            }
            try {
                mEqualizerHelper.setBandLevel((int) band, (int) level);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
