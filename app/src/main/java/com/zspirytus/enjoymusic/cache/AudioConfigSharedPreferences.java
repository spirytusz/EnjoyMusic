package com.zspirytus.enjoymusic.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;

import com.zspirytus.enjoymusic.global.MainApplication;

public class AudioConfigSharedPreferences {

    private static final String TAG = "AudioConfigSharedPrefer";

    private static final String AUDIO_FIELD_KEY = "audioFieldKey";
    private static final String ACOUSTIC_ECHO_CANCELER_ENABLE_KEY = "AcousticEchoCancelerEnableKey";
    private static final String AUTOMATIC_GAIN_CONTROL_ENABLE_KEY = "AutomaticGainControlEnableKey";
    private static final String NOISE_SUPPRESSOR_ENABLE_KEY = "NoiseSuppressorEnableKey";
    private static final String BASS_BOAST_STRENGTH_KEY = "bassBoastStrengthKey";

    private static final int DEFAULT_AUDIO_FIELD_POSITION = 0;

    private AudioConfigSharedPreferences() {
    }

    public static int restoreAudioField() {
        return getSharePreferences().getInt(AUDIO_FIELD_KEY, DEFAULT_AUDIO_FIELD_POSITION);
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveAudioField(int position) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putInt(AUDIO_FIELD_KEY, position);
        editor.commit();
    }

    public static boolean obtainAcousticEchoCancelerEnable() {
        return getSharePreferences().getBoolean(ACOUSTIC_ECHO_CANCELER_ENABLE_KEY, false);
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveAcousticEchoCancelerEnable(boolean isAvailable) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putBoolean(ACOUSTIC_ECHO_CANCELER_ENABLE_KEY, isAvailable);
        editor.commit();
    }

    public static boolean obtainAutomaticGainControlEnable() {
        return getSharePreferences().getBoolean(AUTOMATIC_GAIN_CONTROL_ENABLE_KEY, false);
    }


    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveAutomaticGainControlEnable(boolean isAvailable) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putBoolean(AUTOMATIC_GAIN_CONTROL_ENABLE_KEY, isAvailable);
        editor.commit();
    }

    public static boolean obtainNoiseSuppressorEnable() {
        return getSharePreferences().getBoolean(NOISE_SUPPRESSOR_ENABLE_KEY, false);
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveNoiseSuppressorEnable(boolean isAvailable) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putBoolean(NOISE_SUPPRESSOR_ENABLE_KEY, isAvailable);
        editor.commit();
    }

    public static short obtainBassBoastStrength() {
        return (short) getSharePreferences().getInt(BASS_BOAST_STRENGTH_KEY, 0);
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveBassBoastStrength(short strength) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putInt(BASS_BOAST_STRENGTH_KEY, strength);
        editor.commit();
    }

    private static SharedPreferences getSharePreferences() {
        return MainApplication.getAppContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }
}
