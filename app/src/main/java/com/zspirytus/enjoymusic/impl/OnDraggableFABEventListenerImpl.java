package com.zspirytus.enjoymusic.impl;

import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
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
        if (!AllMusicCache.getInstance().getAllMusicListWithoutScanning().isEmpty()) {
            if (ForegroundMusicController.getInstance().isPlaying()) {
                ForegroundMusicController.getInstance().pause();
            } else {
                ForegroundMusicController.getInstance().play(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
            }
        }
    }

    @Override
    public void onLongClick() {
        if (!AllMusicCache.getInstance().getAllMusicListWithoutScanning().isEmpty()) {
            EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicPlayFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
        }
    }

    @Override
    public void onDraggedLeft() {
        if (!AllMusicCache.getInstance().getAllMusicListWithoutScanning().isEmpty()) {
            ForegroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getPreviousMusic());
        }
    }

    @Override
    public void onDraggedRight() {
        if (!AllMusicCache.getInstance().getAllMusicListWithoutScanning().isEmpty()) {
            ForegroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic());
        }
    }
}
