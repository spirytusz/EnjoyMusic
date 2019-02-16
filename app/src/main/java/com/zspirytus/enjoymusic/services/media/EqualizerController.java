package com.zspirytus.enjoymusic.services.media;

import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.support.annotation.NonNull;

public class EqualizerController {

    private Equalizer mEqualizer;

    private static class Singleton {
        static EqualizerController INSTANCE = new EqualizerController();
    }

    private EqualizerController() {
    }

    static void attachToMediaPlayer(@NonNull MediaPlayer mediaPlayer) {
        Singleton.INSTANCE.mEqualizer = new Equalizer(0, mediaPlayer.getAudioSessionId());
        Singleton.INSTANCE.mEqualizer.setEnabled(true);
        short band = Singleton.INSTANCE.mEqualizer.getNumberOfBands();
        short minRange = Singleton.INSTANCE.mEqualizer.getBandLevelRange()[0];
        short maxRange = Singleton.INSTANCE.mEqualizer.getBandLevelRange()[1];
    }

    public static void setBandLevel(short band, short level) {
        Singleton.INSTANCE.mEqualizer.setBandLevel(band, level);
    }

}
