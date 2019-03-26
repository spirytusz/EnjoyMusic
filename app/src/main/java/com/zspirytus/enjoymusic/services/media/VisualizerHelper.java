package com.zspirytus.enjoymusic.services.media;

import android.media.audiofx.Visualizer;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.foregroundobserver.IFrequencyObserver;
import com.zspirytus.enjoymusic.listeners.observable.FrequencyObservable;

public class VisualizerHelper extends FrequencyObservable implements Visualizer.OnDataCaptureListener {

    private static class Singleton {
        static VisualizerHelper INSTANCE = new VisualizerHelper();
    }

    private static final int STEP = 1 << 6;
    private Visualizer mVisualizer;

    private VisualizerHelper() {
        mVisualizer = new Visualizer(MediaPlayController.getInstance().getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);
    }

    @Override
    public void register(IFrequencyObserver observer) {
        callbackList.register(observer);
    }

    @Override
    public void unregister(IFrequencyObserver observer) {
        callbackList.unregister(observer);
    }

    @Override
    public void notifyAllObserverFrequencyChange(float[] magnitudes, float[] phases) {
        int size = callbackList.beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                callbackList.getBroadcastItem(i).onFrequencyChange(magnitudes);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        callbackList.finishBroadcast();
    }

    public static VisualizerHelper getInstance() {
        return Singleton.INSTANCE;
    }

    public static int getCaptureSize() {
        return Visualizer.getCaptureSizeRange()[1] / STEP;
    }

    static void setEnable(boolean enable) {
        getInstance().mVisualizer.setEnabled(enable);
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        float[] magnitudes = new float[fft.length / STEP];
        float[] phases = new float[fft.length / STEP];
        for (int i = 0; i < fft.length; i += STEP) {
            magnitudes[i / STEP] = (float) Math.hypot(fft[i], fft[i + 1]);
            phases[i / STEP] = (float) Math.atan2(fft[i + 1], fft[i]);
        }
        notifyAllObserverFrequencyChange(magnitudes, phases);
    }
}
