package com.zspirytus.enjoymusic.engine;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;

import com.zspirytus.enjoymusic.IMusicControl;
import com.zspirytus.enjoymusic.IMusicProgressControl;
import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.impl.binder.MusicController;
import com.zspirytus.enjoymusic.impl.binder.MusicProgressControl;
import com.zspirytus.enjoymusic.impl.binder.PlayListSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台音乐播放控制器
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController {

    private static class SingletonHolder {
        private static ForegroundMusicController INSTANCE = new ForegroundMusicController();
    }

    private ForegroundMusicController() {
    }

    public static ForegroundMusicController getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void play(final Music music) {
        if (music != null) {
            ThreadPool.execute(() -> {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                IMusicControl mIMusicControl = MusicController.asInterface(musicControlBinder);
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
            IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
            IMusicControl mIMusicControl = MusicController.asInterface(musicControlBinder);
            try {
                mIMusicControl.playPrevious();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void playNext(boolean fromUser) {
        ThreadPool.execute(() -> {
            IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
            IMusicControl mIMusicControl = MusicController.asInterface(musicControlBinder);
            try {
                mIMusicControl.playNext(fromUser);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void pause() {
        ThreadPool.execute(() -> {
            IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
            IMusicControl mIMusicControl = MusicController.asInterface(musicControlBinder);
            try {
                mIMusicControl.pause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void seekTo(final int milliseconds) {
        ThreadPool.execute(() -> {
            IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_PROGRESS_CONTROL);
            IMusicProgressControl mIMusicProgressControl = MusicProgressControl.asInterface(musicControlBinder);
            try {
                mIMusicProgressControl.seekTo(milliseconds);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    public void setPlayList(final List<Music> musicList) {
        if (musicList != null) {
            ThreadPool.execute(() -> {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                ISetPlayList mISetPlayList = PlayListSetter.asInterface(musicControlBinder);
                try {
                    mISetPlayList.setPlayList(musicList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @WorkerThread
    public Music setPlayListAndGetFirstMusic(List<Music> musicList) {
        if (musicList != null) {
            IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
            ISetPlayList mISetPlayList = PlayListSetter.asInterface(musicControlBinder);
            try {
                return mISetPlayList.setPlayListAndGetFirstMusic(musicList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addToPlayList(final List<Music> musicList) {
        if (musicList != null) {
            ThreadPool.execute(() -> {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                ISetPlayList mISetPlayList = PlayListSetter.asInterface(musicControlBinder);
                try {
                    mISetPlayList.appendMusicList(musicList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void addToPlayList(final Music music) {
        if (music != null) {
            List<Music> musicList = new ArrayList<>();
            musicList.add(music);
            addToPlayList(musicList);
        }
    }

    public void setPlayMode(final int playMode) {
        if (playMode >= 0 && playMode < 4) {
            ThreadPool.execute(() -> {
                IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                IMusicControl mIMusicControl = MusicController.asInterface(musicControlBinder);
                try {
                    mIMusicControl.setPlayMode(playMode);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

}
