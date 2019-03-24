package com.zspirytus.enjoymusic.impl.binder;

import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.global.MainApplication;

public class BinderPool extends IBinderPool.Stub {

    @Override
    public IBinder queryBinder(int binderCode) {
        switch (binderCode) {
            case Constant.BinderCode.MUSIC_CONTROL:
                return MusicController.getInstance();
            case Constant.BinderCode.MUSIC_PROGRESS_CONTROL:
                return MusicProgressControl.getInstance();
            case Constant.BinderCode.GET_MUSIC_LIST:
                return MusicListGetter.getInstance();
            case Constant.BinderCode.SET_PLAY_LIST:
                return PlayListSetter.getInstance();
            case Constant.BinderCode.BACKGROUND_EVENT_PROCESSOR:
                return BackgroundEventProcessor.getInstance();
            case Constant.BinderCode.AUDIO_EFFECT:
                return AudioEffectHelper.getInstance();
            case Constant.BinderCode.MUSIC_META_DATA_UPDATOR:
                return MusicMetaDataUpdator.getInstance();
        }
        return null;
    }

    /**
     * 在这里检查权限
     */
    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        int check = MainApplication.getAppContext().checkCallingOrSelfPermission("com.zspirytus.permission.ACCESS_PLAY_SERVICE");
        if (check == PackageManager.PERMISSION_DENIED) {
            return false;
        }

        String packageName = null;
        //获取客户端包名
        String[] packages = MainApplication.getAppContext().getPackageManager().getPackagesForUid(getCallingUid());
        if (packages != null && packages.length > 0) {
            packageName = packages[0];
        }
        //校验包名
        if (!packageName.startsWith("com.zspirytus")) {
            return false;
        }
        return super.onTransact(code, data, reply, flags);
    }
}
