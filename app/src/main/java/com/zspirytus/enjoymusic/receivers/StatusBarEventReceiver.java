package com.zspirytus.enjoymusic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.engine.BackgroundMusicController;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.services.NotificationHelper;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

/**
 * Created by ZSpirytus on 2018/9/8.
 */

public class StatusBarEventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String event = intent.getStringExtra(FinalValue.StatusBarEvent.EXTRA);
        if (event != null) {
            switch (event) {
                case FinalValue.StatusBarEvent.SINGLE_CLICK:
                    System.out.println(FinalValue.StatusBarEvent.SINGLE_CLICK);
                    break;
                case FinalValue.StatusBarEvent.PREVIOUS:
                    BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getPreviousMusic());
                    break;
                case FinalValue.StatusBarEvent.PLAY_OR_PAUSE:
                    if (MediaPlayController.getInstance().isPlaying()) {
                        BackgroundMusicController.getInstance().pause(MusicCache.getInstance().getCurrentPlayingMusic());
                        NotificationHelper.getInstance().setPlayOrPauseBtnRes(R.drawable.ic_play_arrow_black_48dp);
                    } else {
                        BackgroundMusicController.getInstance().play(MusicCache.getInstance().getCurrentPlayingMusic());
                        NotificationHelper.getInstance().setPlayOrPauseBtnRes(R.drawable.ic_pause_black_48dp);
                    }
                    break;
                case FinalValue.StatusBarEvent.NEXT:
                    BackgroundMusicController.getInstance().play(MusicPlayOrderManager.getInstance().getNextMusic());
                    break;
            }
        }
    }
}
