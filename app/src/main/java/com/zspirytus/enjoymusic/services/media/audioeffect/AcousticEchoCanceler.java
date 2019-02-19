package com.zspirytus.enjoymusic.services.media.audioeffect;

import com.zspirytus.enjoymusic.services.media.MediaPlayController;

// 回声消除器
public class AcousticEchoCanceler {

    private static android.media.audiofx.AcousticEchoCanceler mAcousticEchoCanceler;

    public static boolean isAcousticEchoCancelerAvailable() {
        int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
        return android.media.audiofx.AcousticEchoCanceler.isAvailable()
                && android.media.audiofx.AcousticEchoCanceler.create(audioSessionId) != null;
    }

    public static void setAcousticEchoCancelerEnable(boolean enable) {
        if (mAcousticEchoCanceler == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mAcousticEchoCanceler = android.media.audiofx.AcousticEchoCanceler.create(audioSessionId);
        }
        mAcousticEchoCanceler.setEnabled(enable);
    }

}
