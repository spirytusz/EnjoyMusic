package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import org.simple.eventbus.EventBus;

import java.util.List;

/** 前台音乐播放控制器
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

    public void play(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PLAY);
    }

    public void pause(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.PAUSE);
    }

    public void stop(Music music) {
        EventBus.getDefault().post(music, Constant.EventBusTag.STOP);
    }

    public void seekTo(int msec) {
        EventBus.getDefault().post(msec, Constant.EventBusTag.SEEK_TO);
    }

    public void release() {
        SingletonHolder.INSTANCE = null;
    }

    public boolean isPlaying() {
        return MediaPlayController.getInstance().isPlaying();
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
