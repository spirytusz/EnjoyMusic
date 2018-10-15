package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.mylibrary.DraggableFloatingActionButton;

/**
 * Created by ZSpirytus on 2018/10/16.
 */

public class CustomDFab extends DraggableFloatingActionButton implements MusicPlayStateObserver {
    public CustomDFab(Context context) {
        this(context, null);
    }

    public CustomDFab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomDFab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        registerMusicPlayStateObserver();
    }

    private void registerMusicPlayStateObserver() {
        MediaPlayController.getInstance().registerMusicPlayStateObserver(this);
    }

    public void unregisterMusicPlayStateObserver() {
        MediaPlayController.getInstance().unregisterMusicPlayStateObserver(this);
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        int resId = isPlaying ? R.drawable.ic_pause_white_48dp : R.drawable.ic_play_arrow_white_48dp;
        setImageResource(resId);
    }
}
