package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZSpirytus on 2018/9/13.
 */

public class Artist implements Parcelable {

    private String mArtistName;
    private int mMusicCount = 1;

    public Artist(String artistName) {
        this.mArtistName = artistName;
    }

    private Artist(Parcel source) {
        mArtistName = source.readString();
        mMusicCount = source.readInt();
    }

    public String getArtistName() {
        return mArtistName;
    }

    public int getMusicCount() {
        return mMusicCount;
    }

    public void increaseMusicCount() {
        mMusicCount++;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mArtistName);
        dest.writeInt(mMusicCount);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Artist && mArtistName.equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return mArtistName.hashCode();
    }

    @Override
    public String toString() {
        return mArtistName;
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[0];
        }
    };
}
