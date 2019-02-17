package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.impl.binder.IMusicControlImpl;
import com.zspirytus.enjoymusic.impl.binder.IMusicProgressControlImpl;
import com.zspirytus.enjoymusic.impl.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;

/**
 * 前台音乐播放控制器
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController implements MusicPlayStateObserver {

    private IMusicControl mIMusicControl;
    private IMusicProgressControl mIMusicProgressControl;
    private ISetPlayList mISetPlayList;

    private boolean isPlaying = false;

    private static class SingletonHolder {
        private static ForegroundMusicController INSTANCE = new ForegroundMusicController();
    }

    private ForegroundMusicController() {
        IPlayStateChangeObserverImpl.getInstance().register(this);
    }

    public static ForegroundMusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void play(final Music music) {
        if (music != null) {
            ThreadPool.execute(() -> {
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
                }
                try {
                    mIMusicControl.play(music);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void playPrevious() {
        ThreadPool.execute(() -> {
            if (mIMusicControl == null) {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
            }
            try {
                mIMusicControl.playPrevious();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void playNext(boolean fromUser) {
        ThreadPool.execute(() -> {
            if (mIMusicControl == null) {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
            }
            try {
                mIMusicControl.playNext(fromUser);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void pause() {
        ThreadPool.execute(() -> {
            if (mIMusicControl == null) {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
            }
            try {
                mIMusicControl.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void seekTo(final int milliseconds) {
        ThreadPool.execute(() -> {
            if (mIMusicProgressControl == null) {
                IBinder musicProgressControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_PROGRESS_CONTROL);
                mIMusicProgressControl = IMusicProgressControlImpl.asInterface(musicProgressControlBinder);
            }
            try {
                mIMusicProgressControl.seekTo(milliseconds);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setPlayList(final MusicFilter musicFilter) {
        if (musicFilter != null) {
            ThreadPool.execute(() -> {
                if (mISetPlayList == null) {
                    IBinder setPlayListBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                    mISetPlayList = ISetPlayList.Stub.asInterface(setPlayListBinder);
                }
                try {
                    mISetPlayList.setPlayList(musicFilter);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void setPlayMode(final int playMode) {
        if (playMode >= 0 && playMode < 4) {
            ThreadPool.execute(() -> {
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = IMusicControlImpl.asInterface(musicControlBinder);
                }
                try {
                    mIMusicControl.setPlayMode(playMode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void release() {
        mIMusicControl = null;
        mIMusicProgressControl = null;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

}
