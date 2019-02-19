package com.zspirytus.enjoymusic.services.media.audioeffect;

import com.zspirytus.enjoymusic.services.media.MediaPlayController;

// 噪音抑制器
public class NoiseSuppressor {

    private static android.media.audiofx.NoiseSuppressor mNoiseSuppressor;

    public static boolean isNoiseSuppressorAvailable() {
        int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
        return android.media.audiofx.NoiseSuppressor.isAvailable()
                && android.media.audiofx.NoiseSuppressor.create(audioSessionId) != null;
    }

    public static void setNoiseSuppressorEnable(boolean enable) {
        if (mNoiseSuppressor == null) {
            int audioSessionId = MediaPlayController.getInstance().getAudioSessionId();
            mNoiseSuppressor = android.media.audiofx.NoiseSuppressor.create(audioSessionId);
        }
        mNoiseSuppressor.setEnabled(enable);
    }
}
