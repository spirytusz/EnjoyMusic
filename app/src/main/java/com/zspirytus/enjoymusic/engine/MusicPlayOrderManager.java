package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayOrderManager {

    private static class SingletonHolder {
        static final MusicPlayOrderManager INSTANCE = new MusicPlayOrderManager();
    }

    private List<Music> mPlayList;

    private MusicPlayOrderManager() {
    }

    public static MusicPlayOrderManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setPlayList(List<Music> playList) {
        mPlayList = playList;
    }


    public Music getNextMusic() {
        int currentPosition = mPlayList.indexOf(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
        int nextPosition = (currentPosition + 1) % mPlayList.size();
        Music nextMusic = mPlayList.get(nextPosition);
        return nextMusic != null ? nextMusic : mPlayList.get(0);
    }

    public Music getPreviousMusic() {
        int currentPosition = mPlayList.indexOf(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
        int previousPosition = currentPosition - 1 >= 0 ? currentPosition - 1 : currentPosition - 1 + mPlayList.size();
        Music previousMusic = mPlayList.get(previousPosition);
        return previousMusic != null ? previousMusic : mPlayList.get(0);
    }

}
