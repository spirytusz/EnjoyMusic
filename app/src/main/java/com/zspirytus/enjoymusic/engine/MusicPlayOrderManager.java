package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.MusicSharedPreferences;
import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.cache.PlayHistoryCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.utils.RandomUtil;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayOrderManager {

    private int mPlayMode;

    private static class SingletonHolder {
        static final MusicPlayOrderManager INSTANCE = new MusicPlayOrderManager();
    }

    private List<Music> mPlayList;

    private MusicPlayOrderManager() {
        int restorePlayMode = MusicSharedPreferences.restorePlayMode(MyApplication.getBackgroundContext());
        if (restorePlayMode != -1)
            setPlayMode(restorePlayMode);
    }

    public static MusicPlayOrderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setPlayList(List<Music> playList) {
        mPlayList = playList;
    }

    public Music getNextMusic() {
        Music nextMusic = null;
        if (mPlayMode == Constant.PlayMode.LIST_LOOP || mPlayMode == Constant.PlayMode.SINGLE_LOOP) {
            // 列表循环 || 单曲循环
            int currentPosition = mPlayList.indexOf(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
            int nextPosition = (currentPosition + 1) % mPlayList.size();
            nextMusic = mPlayList.get(nextPosition);
        } else if (mPlayMode == Constant.PlayMode.RANDOM) {
            // 随机播放
            int nextPosition = RandomUtil.rand(mPlayList.size());
            nextMusic = mPlayList.get(nextPosition);
        } else {
            // 列表播放一次
            int currentPosition = mPlayList.indexOf(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
            int nextPosition = (currentPosition + 1) % mPlayList.size();
            if (nextPosition != 0) {
                nextMusic = mPlayList.get(nextPosition);
            }
        }
        return nextMusic;
    }

    public Music getPreviousMusic() {
        return PlayHistoryCache.getInstance().getPreviousPlayedMusic();
    }

    public void setPlayMode(int playMode) {
        mPlayMode = playMode;
    }

}
