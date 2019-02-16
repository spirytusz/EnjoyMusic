package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class EqualizerMetaData implements Parcelable {

    private short band;
    private short minRange;
    private short maxRange;
    private int[] centerFreq;

    public EqualizerMetaData(Parcel source) {
        band = (short) source.readInt();
        maxRange = (short) source.readInt();
        minRange = (short) source.readInt();
        int length = source.readInt();
        if (length > 0) {
            centerFreq = new int[length];
            for (int i = 0; i < length; i++) {
                centerFreq[i] = source.readInt();
            }
        }
    }

    public EqualizerMetaData() {
    }

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

    public int[] getCenterFreq() {
        return centerFreq;
    }

    public void setCenterFreq(int[] centerFreq) {
        this.centerFreq = centerFreq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(band);
        dest.writeInt(maxRange);
        dest.writeInt(minRange);
        if (centerFreq != null) {
            dest.writeInt(centerFreq.length);
            for (int i = 0; i < centerFreq.length; i++) {
                dest.writeInt(centerFreq[i]);
            }
        } else {
            dest.writeInt(0);
        }
    }

    public static final Parcelable.Creator<EqualizerMetaData> CREATOR = new Parcelable.Creator<EqualizerMetaData>() {
        @Override
        public EqualizerMetaData createFromParcel(Parcel source) {
            return new EqualizerMetaData(source);
        }

        @Override
        public EqualizerMetaData[] newArray(int size) {
            return new EqualizerMetaData[0];
        }
    };
}
