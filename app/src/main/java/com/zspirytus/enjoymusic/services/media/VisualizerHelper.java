package com.zspirytus.enjoymusic.services.media;

import android.media.audiofx.Visualizer;

import com.zspirytus.enjoymusic.utils.LogUtil;

public class VisualizerHelper implements Visualizer.OnDataCaptureListener {

    private static class Singleton {
        static VisualizerHelper INSTANCE = new VisualizerHelper();
    }

    private Visualizer mVisualizer;

    private VisualizerHelper() {
        mVisualizer = new Visualizer(MediaPlayController.getInstance().getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);
    }

    private static VisualizerHelper getInstance() {
        return Singleton.INSTANCE;
    }

    public static void setEnable(boolean enable) {
        getInstance().mVisualizer.setEnabled(enable);
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {

    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
        for (byte fftData : fft) {
            LogUtil.e(this.getClass().getSimpleName(), "fft = " + fftData);
        }
        LogUtil.e(this.getClass().getSimpleName(), "\n\n\n\n");
    }
}
