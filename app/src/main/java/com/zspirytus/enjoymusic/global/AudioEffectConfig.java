package com.zspirytus.enjoymusic.global;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class AudioEffectConfig {

    private static final String ACOUSTIC_ECHO_CANCELER_AVAILABLE = "AcousticEchoCancelerAvailable";
    private static final String AUTOMATIC_GAIN_CONTROL_AVAILABLE = "AutomaticGainControlAvailable";
    private static final String NOISE_SUPPRESSOR_AVAILABLE = "NoiseSuppressorAvailable";
    private static final String PRESET_REVERB_NAME_LIST = "PresetReverbNameList";

    private static final String ACOUSTIC_ECHO_CANCELER_ENABLE = "AcousticEchoCancelerEnable";
    private static final String AUTOMATIC_GAIN_CONTROL_ENABLE = "AutomaticGainControlEnable";
    private static final String NOISE_SUPPRESSOR_ENABLE = "NoiseSuppressorEnable";
    private static final String BASS_BOAST_STRENGTH = "bassBoastStrength";

    private AudioEffectConfig() {
    }

    private static boolean isAcousticEchoCancelerAvailable;
    private static boolean isAutomaticGainControlAvailable;
    private static boolean isNoiseSuppressorAvailable;
    private static List<String> presetReverbNameList;

    private static boolean isAcousticEchoCancelerEnable = false;
    private static boolean isAutomaticGainControlEnable = false;
    private static boolean isNoiseSuppressorEnable = false;
    private static short bassBoastStrength;

    public static boolean isIsAcousticEchoCancelerAvailable() {
        return isAcousticEchoCancelerAvailable;
    }

    public static void setIsAcousticEchoCancelerAvailable(boolean isAcousticEchoCancelerAvailable) {
        AudioEffectConfig.isAcousticEchoCancelerAvailable = isAcousticEchoCancelerAvailable;
    }

    public static boolean isIsAutomaticGainControlAvailable() {
        return isAutomaticGainControlAvailable;
    }

    public static void setIsAutomaticGainControlAvailable(boolean isAutomaticGainControlAvailable) {
        AudioEffectConfig.isAutomaticGainControlAvailable = isAutomaticGainControlAvailable;
    }

    public static boolean isIsNoiseSuppressorAvailable() {
        return isNoiseSuppressorAvailable;
    }

    public static void setIsNoiseSuppressorAvailable(boolean isNoiseSuppressorAvailable) {
        AudioEffectConfig.isNoiseSuppressorAvailable = isNoiseSuppressorAvailable;
    }

    public static List<String> getPresetReverbNameList() {
        return presetReverbNameList;
    }

    public static void setPresetReverbNameList(List<String> presetReverbNameList) {
        AudioEffectConfig.presetReverbNameList = presetReverbNameList;
    }

    public static boolean isAcousticEchoCancelerEnable() {
        return isAcousticEchoCancelerEnable;
    }

    public static boolean isAutomaticGainControlEnable() {
        return isAutomaticGainControlEnable;
    }

    public static boolean isNoiseSuppressorEnable() {
        return isNoiseSuppressorEnable;
    }

    public static void setIsAcousticEchoCancelerEnable(boolean isAcousticEchoCancelerEnable) {
        AudioEffectConfig.isAcousticEchoCancelerEnable = isAcousticEchoCancelerEnable;
    }

    public static void setIsAutomaticGainControlEnable(boolean isAutomaticGainControlEnable) {
        AudioEffectConfig.isAutomaticGainControlEnable = isAutomaticGainControlEnable;
    }

    public static void setIsNoiseSuppressorEnable(boolean isNoiseSuppressorEnable) {
        AudioEffectConfig.isNoiseSuppressorEnable = isNoiseSuppressorEnable;
    }

    public static void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(ACOUSTIC_ECHO_CANCELER_AVAILABLE, isAcousticEchoCancelerAvailable);
        outState.putBoolean(AUTOMATIC_GAIN_CONTROL_AVAILABLE, isAutomaticGainControlAvailable);
        outState.putBoolean(NOISE_SUPPRESSOR_AVAILABLE, isNoiseSuppressorAvailable);
        outState.putStringArrayList(PRESET_REVERB_NAME_LIST, (ArrayList<String>) presetReverbNameList);

        outState.putBoolean(ACOUSTIC_ECHO_CANCELER_ENABLE, isAcousticEchoCancelerEnable);
        outState.putBoolean(AUTOMATIC_GAIN_CONTROL_ENABLE, isAcousticEchoCancelerEnable);
        outState.putBoolean(NOISE_SUPPRESSOR_ENABLE, isNoiseSuppressorEnable);
    }

    public static void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isAcousticEchoCancelerAvailable = savedInstanceState.getBoolean(ACOUSTIC_ECHO_CANCELER_AVAILABLE);
            isAutomaticGainControlAvailable = savedInstanceState.getBoolean(AUTOMATIC_GAIN_CONTROL_AVAILABLE);
            isNoiseSuppressorAvailable = savedInstanceState.getBoolean(NOISE_SUPPRESSOR_AVAILABLE);
            presetReverbNameList = savedInstanceState.getStringArrayList(PRESET_REVERB_NAME_LIST);

            isAcousticEchoCancelerEnable = savedInstanceState.getBoolean(ACOUSTIC_ECHO_CANCELER_ENABLE);
            isAutomaticGainControlEnable = savedInstanceState.getBoolean(AUTOMATIC_GAIN_CONTROL_ENABLE);
            isNoiseSuppressorEnable = savedInstanceState.getBoolean(NOISE_SUPPRESSOR_ENABLE);
        }
    }
}
