package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZSpirytus on 2018/9/13.
 */

public class Artist implements Parcelable {

    private long _id;

    private String mArtistName;
    private int mNumberOfAlbums;
    private int mNumberOfTracks;

    public Artist(long id, String artistName, int numberOfAlbums, int numberOfTracks) {
        _id = id;
        mArtistName = artistName;
        mNumberOfAlbums = numberOfAlbums;
        mNumberOfTracks = numberOfTracks;
    }

    private Artist(Parcel source) {
        _id = source.readLong();
        mArtistName = source.readString();
        mNumberOfAlbums = source.readInt();
        mNumberOfTracks = source.readInt();
    }

    public long get_id() {
        return _id;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public int getNumberOfAlbums() {
        return mNumberOfAlbums;
    }

    public int getNumberOfTracks() {
        return mNumberOfTracks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_id);
        dest.writeString(mArtistName);
        dest.writeInt(mNumberOfAlbums);
        dest.writeInt(mNumberOfTracks);
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
