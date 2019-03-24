package com.zspirytus.enjoymusic.services.media.audioeffect;

import android.media.audiofx.Equalizer;

import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;

import java.util.ArrayList;
import java.util.List;

// 均衡器
public class EqualizerController {

    private Equalizer mEqualizer;

    private static class Singleton {
        static EqualizerController INSTANCE = new EqualizerController();
    }

    private EqualizerController() {
        mEqualizer = new Equalizer(0, MediaPlayController.getInstance().getAudioSessionId());
        mEqualizer.setEnabled(true);
    }

    public static EqualizerMetaData attachToMediaPlayer() {
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
    }

    public static int[] getBandLevel() {
        int bands = Singleton.INSTANCE.mEqualizer.getNumberOfBands();
        int[] bandAndLevel = new int[bands];
        for (int i = 0; i < bandAndLevel.length; i++) {
            bandAndLevel[i] = Singleton.INSTANCE.mEqualizer.getBandLevel((short) i);
        }
        return bandAndLevel;
    }

    static List<String> getPresetNameList() {
        int size = Singleton.INSTANCE.mEqualizer.getNumberOfPresets();
        List<String> nameList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            nameList.add(Singleton.INSTANCE.mEqualizer.getPresetName((short) i));
        }
        return nameList;
    }

    static void usePresetReverb(int position) {
        Singleton.INSTANCE.mEqualizer.usePreset((short) position);
    }

}
