package com.zspirytus.enjoymusic.services.media.audioeffect;

import com.zspirytus.enjoymusic.services.media.MediaPlayController;

// 自动增强控制器
public class AutomaticGainControl {

    private static android.media.audiofx.AutomaticGainControl mAutomaticGainControl;

    public static boolean isAutomaticGainControlAvailable() {
        return android.media.audiofx.AutomaticGainControl.isAvailable();
    }

    public static void setAutomaticGainControlEnable(boolean enable) {
        if (mAutomaticGainControl == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mAutomaticGainControl = android.media.audiofx.AutomaticGainControl.create(audioSessionId);
        }
        mAutomaticGainControl.setEnabled(enable);
    }
}
