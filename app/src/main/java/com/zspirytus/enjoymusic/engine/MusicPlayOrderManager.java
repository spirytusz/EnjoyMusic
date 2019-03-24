package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.BackgroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.PlayList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinPlayListToMusic;
import com.zspirytus.enjoymusic.listeners.observable.PlayListChangeObservable;
import com.zspirytus.enjoymusic.utils.RamdomNumberGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayOrderManager extends PlayListChangeObservable {

    private static class SingletonHolder {
        static final MusicPlayOrderManager INSTANCE = new MusicPlayOrderManager();
    }

    private int mPlayMode;

    private MusicPlayOrderManager() {
        int restorePlayMode = MusicSharedPreferences.restorePlayMode();
        if (restorePlayMode != -1) {
            setPlayMode(restorePlayMode);
        }
        PlayList playList = DBManager.getInstance().getDaoSession().load(PlayList.class, PLAY_LIST_PRIMARY_KEY);
        if (playList != null) {
            mPlayList = playList.getPlayList();
        } else {
            mPlayList = new ArrayList<>();
        }
    }

    public static MusicPlayOrderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Music> getPlayList() {
        return mPlayList;
    }

    public void setPlayList(List<Music> playList) {
        orderPlayList(playList);
        notifyAllObserverPlayListChange(mPlayList);
        DBManager.getInstance().getDaoSession().getPlayListDao().deleteAll();
        DBManager.getInstance().getDaoSession().getJoinPlayListToMusicDao().deleteAll();
        long playListId = PLAY_LIST_PRIMARY_KEY;
        PlayList playListTable = new PlayList(playListId);
        List<JoinPlayListToMusic> joinPlayListToMusics = new ArrayList<>();
        for (Music music : mPlayList) {
            JoinPlayListToMusic joinPlayListToMusic = new JoinPlayListToMusic(playListId, music.getMusicId());
            joinPlayListToMusics.add(joinPlayListToMusic);
        }
        DBManager.getInstance().getDaoSession().getPlayListDao().insert(playListTable);
        DBManager.getInstance().getDaoSession().getJoinPlayListToMusicDao().insertInTx(joinPlayListToMusics);
    }

    public void addMusicListToPlayList(List<Music> musicList) {
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
        notifyAllObserverPlayListChange(mPlayList);
        List<JoinPlayListToMusic> joinPlayListToMusics = new ArrayList<>();
        for (Music music : mPlayList) {
            JoinPlayListToMusic joinPlayListToMusic = new JoinPlayListToMusic(PLAY_LIST_PRIMARY_KEY, music.getMusicId());
            joinPlayListToMusics.add(joinPlayListToMusic);
        }
        DBManager.getInstance().getDaoSession().getJoinPlayListToMusicDao().insertOrReplaceInTx(joinPlayListToMusics);
    }

    public Music getNextMusic(boolean fromUser) {
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

    public Music getPreviousMusic() {
        int currentPosition = mPlayList.indexOf(BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic());
        int previousPosition = (currentPosition - 1 + mPlayList.size()) % mPlayList.size();
        return mPlayList.get(previousPosition);
    }

    public void setPlayMode(int playMode) {
        mPlayMode = playMode;
        MusicSharedPreferences.savePlayMode(playMode);
        if (mPlayList != null) {
            List<Music> musicList = new ArrayList<>(mPlayList);
            orderPlayList(musicList);
            notifyAllObserverPlayListChange(mPlayList);
        }
    }

    private void orderPlayList(List<Music> playList) {
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
