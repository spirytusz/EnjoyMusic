package com.zspirytus.enjoymusic.engine;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicProgressControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;

import java.util.List;

/**
 * 前台音乐播放控制器
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController implements MusicPlayStateObserver {

    private static final int BINDER_POOL_CODE_MUSIC_CONTROL = 1;
    private static final int BINDER_POOL_CODE_MUSIC_PROGRESS_CONTROL = 2;

    private IMusicControl mIMusicControl;
    private IMusicProgressControl mIMusicProgressControl;

    private static boolean isPlaying = false;

    private static class SingletonHolder {
        private static ForegroundMusicController INSTANCE = new ForegroundMusicController();
    }

    private ForegroundMusicController() {
        IPlayStateChangeObserverImpl.getInstance().register(this);
    }

    public static ForegroundMusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void play(Music music) {
        if (mIMusicControl == null) {
            mIMusicControl = IMusicControlImpl.asInterface(ForegroundBinderManager.getInstance().getBinderByBinderCode(BINDER_POOL_CODE_MUSIC_CONTROL));
        }
        try {
            mIMusicControl.play(music);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mIMusicControl == null) {
            mIMusicControl = IMusicControlImpl.asInterface(ForegroundBinderManager.getInstance().getBinderByBinderCode(BINDER_POOL_CODE_MUSIC_CONTROL));
        }
        try {
            mIMusicControl.pause();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void seekTo(int msec) {
        if (mIMusicProgressControl == null) {
            mIMusicProgressControl = IMusicProgressControlImpl.asInterface(ForegroundBinderManager.getInstance().getBinderByBinderCode(BINDER_POOL_CODE_MUSIC_PROGRESS_CONTROL));
        }
        try {
            mIMusicProgressControl.seekTo(msec);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void release() {
        SingletonHolder.INSTANCE = null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public List<Music> getAllMusicList() {
        return AllMusicCache.getInstance().getAllMusicList();
    }

    public List<Album> getAlbumList() {
        return AllMusicCache.getInstance().getAlbumList();
    }

    public List<Artist> getArtistList() {
        return AllMusicCache.getInstance().getArtistList();
    }
}
