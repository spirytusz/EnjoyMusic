package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.BackgroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.PlayHistoryCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.listeners.observable.PlayListChangeObservable;
import com.zspirytus.enjoymusic.utils.RandomUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayOrderManager extends PlayListChangeObservable {

    private static class SingletonHolder {
        static final MusicPlayOrderManager INSTANCE = new MusicPlayOrderManager();
    }

    private int mPlayMode;
    private List<Music> mPlayList;

    private MusicPlayOrderManager() {
        int restorePlayMode = MusicSharedPreferences.restorePlayMode(MainApplication.getBackgroundContext());
        if (restorePlayMode != -1) {
            setPlayMode(restorePlayMode);
        }
        mPlayList = MusicSharedPreferences.restorePlayList(MainApplication.getBackgroundContext());
    }

    public static MusicPlayOrderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setPlayList(MusicFilter filter) {
        mPlayList = filter.filter(MusicScanner.getInstance().getAllMusicList());
        notifyAllObserverPlayListChange(filter);
        MusicSharedPreferences.savePlayList(mPlayList);
    }

    public void setPlayList(List<Music> playList) {
        mPlayList = playList;
        MusicSharedPreferences.savePlayList(playList);
        notifyAllObserverPlayListChange(playList);
    }

    public void addMusicListToPlayList(List<Music> musicList) {
        if (mPlayList != null) {
            Set<Music> set = new HashSet<>();
            set.addAll(mPlayList);
            set.addAll(musicList);
            mPlayList = new ArrayList<>(set);
        } else {
            mPlayList = new ArrayList<>();
            mPlayList.addAll(musicList);
        }
        MusicSharedPreferences.savePlayList(mPlayList);
        notifyAllObserverPlayListChange(mPlayList);
    }

    public void addMusicToPlayList(MusicFilter filter) {
        addMusicListToPlayList(filter.filter(MusicScanner.getInstance().getAllMusicList()));
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
                // 列表循环
                int currentPosition = mPlayList.indexOf(BackgroundMusicStateCache.getInstance().getCurrentPlayingMusic());
                nextPosition = (currentPosition + 1) % mPlayList.size();
                nextMusic = mPlayList.get(nextPosition);
                break;
            case Constant.PlayMode.RANDOM:
                // 随机播放
                nextPosition = RandomUtil.rand(mPlayList.size());
                nextMusic = mPlayList.get(nextPosition);
                break;
        }
        return nextMusic;
    }

    public Music getPreviousMusic() {
        return PlayHistoryCache.getInstance().getPreviousPlayedMusic();
    }

    public void setPlayMode(int playMode) {
        mPlayMode = playMode;
        MusicSharedPreferences.savePlayMode(playMode);
    }

}
