package com.zspirytus.enjoymusic.services.media.audioeffect;

import com.zspirytus.enjoymusic.services.media.MediaPlayController;

// 回声消除器
public class AcousticEchoCanceler {

    private static android.media.audiofx.AcousticEchoCanceler mAcousticEchoCanceler;

    public static boolean isAcousticEchoCancelerAvailable() {
        return android.media.audiofx.AcousticEchoCanceler.isAvailable();
    }

    public static void setAcousticEchoCancelerEnable(boolean enable) {
        if (mAcousticEchoCanceler == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mAcousticEchoCanceler = android.media.audiofx.AcousticEchoCanceler.create(audioSessionId);
        }
        mAcousticEchoCanceler.setEnabled(enable);
    }

}
