package com.zspirytus.enjoymusic.impl;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.view.activity.MainActivity;
import com.zspirytus.mylibrary.DraggableFloatingActionButton;
import com.zspirytus.mylibrary.OnDraggableFABEventListener;

/**
 * Created by ZSpirytus on 2018/9/2.
 */

public class OnDraggableFABEventListenerImpl implements OnDraggableFABEventListener {

    private MainActivity mParentActivity;
    private DraggableFloatingActionButton mDFab;

    public OnDraggableFABEventListenerImpl(MainActivity activity) {
        mParentActivity = activity;
        mDFab = activity.findViewById(R.id.dragged_fab);
    }

    @Override
    public void onClick() {
        if (MediaPlayController.getInstance().isPlaying()) {
            mParentActivity.pause(MusicCache.getInstance().getCurrentPlayingMusic());
            mDFab.setClickSrc(R.drawable.ic_play_arrow_white_48dp);
        } else {
            mParentActivity.play(MusicCache.getInstance().getCurrentPlayingMusic());
            mDFab.setClickSrc(R.drawable.ic_pause_white_48dp);
        }
    }

    @Override
    public void onLongClick() {
        mParentActivity.showMusicPlayFragment(MusicCache.getInstance().getCurrentPlayingMusic());
    }

    @Override
    public void onDraggedLeft() {
        mParentActivity.play(MusicCache.getInstance().getPreviousMusic(MusicCache.MODE_ORDER));
    }

    @Override
    public void onDraggedRight() {
        mParentActivity.play(MusicCache.getInstance().getNextMusic(MusicCache.MODE_ORDER));
    }
}
