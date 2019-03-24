package com.zspirytus.enjoymusic.cache;

import android.content.Context;
import android.content.SharedPreferences;

import com.zspirytus.enjoymusic.global.MainApplication;

public class AudioConfigSharedPreferences {

    private static final String TAG = "AudioConfigSharedPrefer";

    private static final String AUDIO_FIELD_KEY = "audioFieldKey";
    /*private static final String ACOUSTIC_ECHO_CANCELER_AVAILABLE_KEY = "AcousticEchoCancelerAvailableKey";
    private static final String AUTOMATIC_GAIN_CONTROL_AVAILABLE_KEY = "AutomaticGainControlAvailableKey";
    private static final String NOISE_SUPPRESSOR_AVAILABLE_KEY = "NoiseSuppressorAvailableKey";*/

    private static final int DEFAULT_AUDIO_FIELD_POSITION = 0;

    private AudioConfigSharedPreferences() {
    }

    public static int restoreAudioField() {
        return getSharePreferences().getInt(AUDIO_FIELD_KEY, DEFAULT_AUDIO_FIELD_POSITION);
    }

    public static void saveAudioField(int position) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putInt(AUDIO_FIELD_KEY, position);
        editor.apply();
    }

    /*public static boolean obtainAcousticEchoCancelerAvailable() {
        return getSharePreferences().getBoolean(ACOUSTIC_ECHO_CANCELER_AVAILABLE_KEY, false);
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveAcousticEchoCancelerAvailable(boolean isAvailable) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putBoolean(ACOUSTIC_ECHO_CANCELER_AVAILABLE_KEY, isAvailable);
        editor.commit();
    }

    public static boolean obtainAutomaticGainControlAvailable() {
        return getSharePreferences().getBoolean(AUTOMATIC_GAIN_CONTROL_AVAILABLE_KEY, false);
    }


    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveAutomaticGainControlAvailable(boolean isAvailable) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putBoolean(AUTOMATIC_GAIN_CONTROL_AVAILABLE_KEY, isAvailable);
        editor.commit();
    }

    public static boolean obtainNoiseSuppressorAvailable() {
        return getSharePreferences().getBoolean(NOISE_SUPPRESSOR_AVAILABLE_KEY, false);
    }

    @SuppressLint("ApplySharedPref")
    @WorkerThread
    public static void saveNoiseSuppressorAvailable(boolean isAvailable) {
        SharedPreferences.Editor editor = getSharePreferences().edit();
        editor.putBoolean(NOISE_SUPPRESSOR_AVAILABLE_KEY, isAvailable);
        editor.commit();
    }
*/
    private static SharedPreferences getSharePreferences() {
        return MainApplication.getAppContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }
}
