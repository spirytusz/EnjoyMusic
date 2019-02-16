package com.zspirytus.enjoymusic.services.media;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.support.annotation.NonNull;

import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.utils.LogUtil;

public class EqualizerController {

    private Equalizer mEqualizer;

    private static class Singleton {
        static EqualizerController INSTANCE = new EqualizerController();
    }

    private EqualizerController() {
    }

    static EqualizerMetaData attachToMediaPlayer(@NonNull MediaPlayer mediaPlayer) {
        Singleton.INSTANCE.mEqualizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        Singleton.INSTANCE.mEqualizer.setEnabled(true);
        short bands = Singleton.INSTANCE.mEqualizer.getNumberOfBands();
        short minRange = Singleton.INSTANCE.mEqualizer.getBandLevelRange()[0];
        short maxRange = Singleton.INSTANCE.mEqualizer.getBandLevelRange()[1];
        int[] centerFreq = new int[bands];
        for (short i = 0; i < bands; i++) {
            centerFreq[i] = Singleton.INSTANCE.mEqualizer.getCenterFreq(i);
        }
        EqualizerMetaData metaData = new EqualizerMetaData();
        metaData.setBand(bands);
        metaData.setMaxRange(maxRange);
        metaData.setMinRange(minRange);
        metaData.setCenterFreq(centerFreq);
        return metaData;
    }

    public static void setBandLevel(short band, short level) {
        Singleton.INSTANCE.mEqualizer.setBandLevel(band, level);
        short newLevel = Singleton.INSTANCE.mEqualizer.getBandLevel(band);
        LogUtil.e(EqualizerController.class.getSimpleName(), "band = " + band + "\tlevel = " + level + "\tnewLevel = " + newLevel);
    }

}
