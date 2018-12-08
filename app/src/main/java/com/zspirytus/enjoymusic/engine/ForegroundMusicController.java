package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IGetMusicList;
import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IMusicProgressControlImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;

/**
 * 前台音乐播放控制器
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController implements MusicPlayStateObserver, PlayedMusicChangeObserver {

    private IMusicControl mIMusicControl;
    private IMusicProgressControl mIMusicProgressControl;
    private IGetMusicList mIGetMusicList;

    private static boolean isPlaying = false;

    private static class SingletonHolder {
        private static ForegroundMusicController INSTANCE = new ForegroundMusicController();
    }

    private ForegroundMusicController() {
        IPlayStateChangeObserverImpl.getInstance().register(this);
        IPlayMusicChangeObserverImpl.getInstance().register(this);
    }

    public static ForegroundMusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void play(final Music music) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
                }
                try {
                    mIMusicControl.play(music);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void playPrevious() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
                }
                try {
                    mIMusicControl.playPrevious();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void playNext() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
                }
                try {
                    mIMusicControl.playNext();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void pause() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
                }
                try {
                    mIMusicControl.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void seekTo(final int milliseconds) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mIMusicProgressControl == null) {
                    IBinder musicProgressControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_PROGRESS_CONTROL);
                    mIMusicProgressControl = IMusicProgressControlImpl.asInterface(musicProgressControlBinder);
                }
                try {
                    mIMusicProgressControl.seekTo(milliseconds);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void release() {
        mIMusicControl = null;
        mIMusicProgressControl = null;
        SingletonHolder.INSTANCE = null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        ForegroundMusicCache.getInstance().setCurrentPlayingMusic(music);
    }

}
