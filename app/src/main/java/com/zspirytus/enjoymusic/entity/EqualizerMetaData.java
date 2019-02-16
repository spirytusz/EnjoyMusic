package com.zspirytus.enjoymusic.entity;

public class EqualizerMetaData {

    private short band;
    private short minRange;
    private short maxRange;
    private short[] centerFreq;

    public short getBand() {
        return band;
    }

    public void setBand(short band) {
        this.band = band;
    }

    public short getMinRange() {
        return minRange;
    }

    public void setMinRange(short minRange) {
        this.minRange = minRange;
    }

    public short getMaxRange() {
        return maxRange;
    }

    public void setMaxRange(short maxRange) {
        this.maxRange = maxRange;
    }

    public short[] getCenterFreq() {
        return centerFreq;
    }

    public void setCenterFreq(short[] centerFreq) {
        this.centerFreq = centerFreq;
    }
}
