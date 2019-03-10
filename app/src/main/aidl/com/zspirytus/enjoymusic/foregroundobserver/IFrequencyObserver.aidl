// IFrequencyObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

// Declare any non-default types here with import statements

interface IFrequencyObserver {
    void onFrequencyChange(in float[] magnitudes, in float[] phases);
}
