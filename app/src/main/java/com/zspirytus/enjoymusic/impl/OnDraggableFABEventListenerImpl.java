package com.zspirytus.enjoymusic.impl;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.mylibrary.DraggableFloatingActionButton;
import com.zspirytus.mylibrary.OnDraggableFABEventListener;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/2.
 */

public class OnDraggableFABEventListenerImpl implements OnDraggableFABEventListener {

    private DraggableFloatingActionButton mDFab;

    public OnDraggableFABEventListenerImpl(DraggableFloatingActionButton dFab) {
        mDFab = dFab;
    }

    @Override
    public void onClick() {
        if (MediaPlayController.getInstance().isPlaying()) {
            ForegroundMusicController.getInstance().pause(MusicCache.getInstance().getCurrentPlayingMusic());
            mDFab.setClickSrc(R.drawable.ic_play_arrow_white_48dp);
        } else {
            ForegroundMusicController.getInstance().play(MusicCache.getInstance().getCurrentPlayingMusic());
            mDFab.setClickSrc(R.drawable.ic_pause_white_48dp);
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
