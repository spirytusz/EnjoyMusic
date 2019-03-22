package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.IMusicMetaDataUpdator;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.global.MainApplication;

public class ForegroundMusicUpdator {

    private static class Singleton {
        static ForegroundMusicUpdator INSTANCE = new ForegroundMusicUpdator();
    }

    private IMusicMetaDataUpdator updator;

    private ForegroundMusicUpdator() {
    }

    public static ForegroundMusicUpdator getInstance() {
        return Singleton.INSTANCE;
    }

    @WorkerThread
    public boolean updateAlbum(Album album) {
        if (album != null) {
            initBinder();
            try {
                return updator.updateAlbum(album);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @WorkerThread
    public void updateArtist(Artist artist) {
        if (artist.peakArtistArt() != null) {
            ThreadPool.execute(() -> {
                initBinder();
                try {
                    updator.updateArtist(artist);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @UiThread
    public void deleteMusic(Music music) {
        if (music != null) {
            ThreadPool.execute(() -> {
                initBinder();
                boolean isSuccess;
                try {
                    isSuccess = updator.deleteMusic(music);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    isSuccess = false;
                }
                ToastUtil.postToShow(MainApplication.getAppContext(), isSuccess ? R.string.success : R.string.failed);
            });
        }
    }

    private synchronized void initBinder() {
        if (updator == null) {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_META_DATA_UPDATOR);
            updator = IMusicMetaDataUpdator.Stub.asInterface(binder);
        }
    }
}
