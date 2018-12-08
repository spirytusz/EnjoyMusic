package com.zspirytus.enjoymusic.impl;

import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.mylibrary.OnDraggableFABEventListener;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/2.
 */

public class OnDraggableFABEventListenerImpl implements OnDraggableFABEventListener {

    @Override
    public void onClick() {
        Music currentPlayingMusic = ForegroundMusicCache.getInstance().getCurrentPlayingMusic();
        if (currentPlayingMusic != null) {
            if (ForegroundMusicController.getInstance().isPlaying()) {
                ForegroundMusicController.getInstance().pause();
            } else {
                ForegroundMusicController.getInstance().play(currentPlayingMusic);
            }
        }
    }

    @Override
    public void onLongClick() {
        EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicPlayFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    @Override
    public void onDraggedLeft() {
        ForegroundMusicController.getInstance().playPrevious();
    }

    @Override
    public void onDraggedRight() {
        ForegroundMusicController.getInstance().playNext();
    }
}
