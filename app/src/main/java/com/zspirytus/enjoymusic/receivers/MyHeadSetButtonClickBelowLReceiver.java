package com.zspirytus.enjoymusic.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

/**
 * Created by ZSpirytus  on 2018/8/11.
 */

public class MyHeadSetButtonClickBelowLReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null || event.getAction() != KeyEvent.ACTION_UP) {
                return;
            }

            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                case KeyEvent.KEYCODE_HEADSETHOOK:
                    // play or pause
                    if (MediaPlayController.getInstance().isPlaying()) {
                        MediaPlayController.getInstance().pause();
                    } else {
                        MediaPlayController.getInstance().play(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    // next
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    // previous
                    break;
            }
        }
    }

}