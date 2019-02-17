package com.zspirytus.enjoymusic.global;

import java.util.List;

@SuppressWarnings("unused")
public class AudioEffectConfig {

    private AudioEffectConfig() {
    }

    private static boolean isAcousticEchoCancelerAvailable;
    private static boolean isAutomaticGainControlAvailable;
    private static boolean isNoiseSuppressorAvailable;
    private static List<String> presetReverbNameList;

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
}
