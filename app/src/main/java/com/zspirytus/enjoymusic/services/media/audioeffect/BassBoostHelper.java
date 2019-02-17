package com.zspirytus.enjoymusic.services.media.audioeffect;

import android.media.audiofx.BassBoost;

import com.zspirytus.enjoymusic.services.media.MediaPlayController;

// 重低音调节器
public class BassBoostHelper {

    private static BassBoost mBassBoost;

    public static void setStrength(short strength) {
        if (mBassBoost == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mBassBoost = new BassBoost(0, audioSessionId);
        }
        mBassBoost.setStrength(strength);
    }
}
