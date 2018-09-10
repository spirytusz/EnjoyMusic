package com.zspirytus.enjoymusic.impl;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.mylibrary.OnDraggableFABEventListener;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/2.
 */

public class OnDraggableFABEventListenerImpl implements OnDraggableFABEventListener {

    @Override
    public void onClick() {
        if (MediaPlayController.getInstance().isPlaying()) {
            ForegroundMusicController.getInstance().pause(MusicCache.getInstance().getCurrentPlayingMusic());
        } else {
            ForegroundMusicController.getInstance().play(MusicCache.getInstance().getCurrentPlayingMusic());
        }
    }

    @Override
    public void onLongClick() {
        EventBus.getDefault().post(MusicCache.getInstance().getCurrentPlayingMusic(), FinalValue.EventBusTag.SHOW_MUSIC_PLAY_FRAGMENT);
    }

    @Override
    public void onDraggedLeft() {
        ForegroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getPreviousMusic());
    }

    @Override
    public void onDraggedRight() {
        ForegroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic());
    }
}
