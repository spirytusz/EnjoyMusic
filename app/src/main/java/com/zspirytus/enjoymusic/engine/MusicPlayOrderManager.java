package com.zspirytus.enjoymusic.engine;

import android.os.RemoteException;

import com.zspirytus.enjoymusic.cache.BackgroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.PlayList;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;
import com.zspirytus.enjoymusic.listeners.observable.RemoteObservable;
import com.zspirytus.enjoymusic.utils.RamdomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayOrderManager extends RemoteObservable<IPlayListChangeObserver, List<Music>> {

    private List<Music> mPlayList;

    private static class SingletonHolder {
        static final MusicPlayOrderManager INSTANCE = new MusicPlayOrderManager();
    }

    private int mPlayMode;

    private MusicPlayOrderManager() {
        int restorePlayMode = MusicSharedPreferences.restorePlayMode();
        if (restorePlayMode != -1) {
            setPlayMode(restorePlayMode);
        }
        mPlayList = QueryExecutor.getPlayList();
    }

    @Override
    public void register(IPlayListChangeObserver observer) {
        getCallbackList().register(observer);
        try {
            observer.onPlayListChange(mPlayList);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unregister(IPlayListChangeObserver observer) {
        getCallbackList().unregister(observer);
    }

    @Override
    protected void notifyChange(List<Music> musicList) {
        int size = getCallbackList().beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                getCallbackList().getBroadcastItem(i).onPlayListChange(musicList);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        getCallbackList().finishBroadcast();
    }

    public static MusicPlayOrderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Music> getPlayList() {
        return mPlayList;
    }

    public synchronized void setPlayList(List<Music> playList) {
        orderPlayList(playList);
        notifyChange(mPlayList);

        DBManager.getInstance().getDaoSession().getPlayListDao().deleteAll();
        List<PlayList> playLists = new ArrayList<>();
        for (Music music : mPlayList) {
            playLists.add(new PlayList(music.getMusicId()));
        }
        DBManager.getInstance().getDaoSession().getPlayListDao().insertInTx(playLists);
    }

    public synchronized void addMusicListToPlayList(List<Music> musicList) {
        if (mPlayList != null) {
            for (Music music : musicList) {
                if (!mPlayList.contains(music)) {
                    mPlayList.add(music);
                }
            }
            List<Music> playList = new ArrayList<>(mPlayList);
            orderPlayList(playList);
        } else {
            mPlayList = new ArrayList<>();
            orderPlayList(musicList);
        }
        notifyChange(mPlayList);
        List<PlayList> playLists = new ArrayList<>();
        for (Music music : mPlayList) {
            playLists.add(new PlayList(music.getMusicId()));
        }
        DBManager.getInstance().getDaoSession().getPlayListDao().insertInTx(playLists);
    }

    public synchronized Music getNextMusic(boolean fromUser) {
        Music nextMusic = null;
        int nextPosition;
        switch (mPlayMode) {
            case Constant.PlayMode.SINGLE_LOOP:
                // 单曲循环
                // 如果是用户操作，逻辑同列表循环
                // 否则，继续播放当前音乐
                if (!fromUser) {
                    nextMusic = BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic();
                    break;
                }
            case Constant.PlayMode.LIST_LOOP:
            case Constant.PlayMode.RANDOM:
                // 随机播放
                // 列表循环
                int currentPosition = mPlayList.indexOf(BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic());
                if (!mPlayList.isEmpty()) {
                    nextPosition = (currentPosition + 1) % mPlayList.size();
                    nextMusic = mPlayList.get(nextPosition);
                } else {
                    nextMusic = BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic();
                }
                break;
        }
        return nextMusic;
    }

    public synchronized Music getPreviousMusic() {
        int currentPosition = mPlayList.indexOf(BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic());
        int previousPosition = (currentPosition - 1 + mPlayList.size()) % mPlayList.size();
        return mPlayList.get(previousPosition);
    }

    public synchronized void setPlayMode(int playMode) {
        mPlayMode = playMode;
        MusicSharedPreferences.savePlayMode(playMode);
        if (mPlayList != null) {
            List<Music> musicList = new ArrayList<>(mPlayList);
            orderPlayList(musicList);
            notifyChange(mPlayList);
        }
    }

    private synchronized void orderPlayList(List<Music> playList) {
        if (mPlayMode == Constant.PlayMode.SINGLE_LOOP) {
            mPlayList = playList;
        } else if (mPlayMode == Constant.PlayMode.LIST_LOOP) {
            mPlayList = playList;
        } else if (mPlayMode == Constant.PlayMode.RANDOM) {
            List<Music> musicList = new ArrayList<>();
            while (!playList.isEmpty()) {
                int randomIndex = RamdomNumberGenerator.rand(playList.size());
                musicList.add(playList.get(randomIndex));
                playList.remove(randomIndex);
            }
            mPlayList = musicList;
        }
    }

}
