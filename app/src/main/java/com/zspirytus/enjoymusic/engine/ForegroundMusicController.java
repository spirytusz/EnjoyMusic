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
import com.zspirytus.enjoymusic.impl.binder.MusicController;
import com.zspirytus.enjoymusic.impl.binder.MusicProgressControl;

import java.util.ArrayList;
import java.util.List;

/**
 * 前台音乐播放控制器
 * Created by ZSpirytus on 2018/9/8.
 */

public class ForegroundMusicController {

    private IMusicControl mIMusicControl;
    private IMusicProgressControl mIMusicProgressControl;
    private ISetPlayList mISetPlayList;

    private boolean isPlaying = false;

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
                if (mIMusicControl == null) {
                    IBinder musicControlBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_CONTROL);
                    mIMusicControl = MusicController.asInterface(musicControlBinder);
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
                mIMusicControl = MusicController.asInterface(musicControlBinder);
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
                mIMusicControl = MusicController.asInterface(musicControlBinder);
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
                mIMusicControl = MusicController.asInterface(musicControlBinder);
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
                mIMusicProgressControl = MusicProgressControl.asInterface(musicProgressControlBinder);
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

    public void setPlayList(final List<Music> musicList) {
        if (musicList != null) {
            ThreadPool.execute(() -> {
                if (mISetPlayList == null) {
                    IBinder setPlayListBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                    mISetPlayList = ISetPlayList.Stub.asInterface(setPlayListBinder);
                }
                try {
                    mISetPlayList.setPlayListDirectly(musicList);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void addToPlayList(final List<Music> musicList) {
        if (musicList != null) {
            ThreadPool.execute(() -> {
                if (mISetPlayList == null) {
                    IBinder setPlayListBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                    mISetPlayList = ISetPlayList.Stub.asInterface(setPlayListBinder);
                }
                try {
                    mISetPlayList.appendMusicListDirectly(musicList);
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

    public void addToPlayList(final MusicFilter filter) {
        if (filter != null) {
            ThreadPool.execute(() -> {
                if (mISetPlayList == null) {
                    IBinder setPlayListBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                    mISetPlayList = ISetPlayList.Stub.asInterface(setPlayListBinder);
                }
                try {
                    mISetPlayList.appendMusic(filter);
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
                    mIMusicControl = MusicController.asInterface(musicControlBinder);
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

}
