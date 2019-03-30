package com.zspirytus.enjoymusic.services.media;

import android.media.audiofx.Visualizer;
import android.os.RemoteException;

import com.zspirytus.enjoymusic.foregroundobserver.IFrequencyObserver;
import com.zspirytus.enjoymusic.listeners.observable.RemoteObservable;

public class VisualizerHelper extends RemoteObservable<IFrequencyObserver, Float[]> implements Visualizer.OnDataCaptureListener {

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
        getCallbackList().register(observer);
    }

    @Override
    public void unregister(IFrequencyObserver observer) {
        getCallbackList().unregister(observer);
    }

    @Override
    protected void notifyChange(Float[] magnitudes) {
        float[] unboxing = new float[magnitudes.length];
        for (int i = 0; i < magnitudes.length; i++) {
            unboxing[i] = magnitudes[i];
        }
        int size = getCallbackList().beginBroadcast();
        for (int i = 0; i < size; i++) {
            try {
                getCallbackList().getBroadcastItem(i).onFrequencyChange(unboxing);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        getCallbackList().finishBroadcast();
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
        Float[] magnitudes = new Float[fft.length / STEP];
        for (int i = 0; i < fft.length; i += STEP) {
            magnitudes[i / STEP] = (float) Math.hypot(fft[i], fft[i + 1]);
        }
        notifyChange(magnitudes);
    }
}
