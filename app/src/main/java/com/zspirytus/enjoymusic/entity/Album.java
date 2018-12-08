package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class Album implements Parcelable {

    private String mAlbumName;
    private String mArtist;
    private String mAlbumCoverPath;

    public Album(String albumName, String albumCoverPath, String artist) {
        mAlbumName = albumName;
        mAlbumCoverPath = albumCoverPath;
        mArtist = artist;
    }

    private Album(Parcel source) {
        mAlbumName = source.readString();
        mArtist = source.readString();
        mAlbumCoverPath = source.readString();
    }

    public String getAlbumCoverPath() {
        return mAlbumCoverPath;
    }

    public void setAlbumCoverPath(String albumCoverPath) {
        this.mAlbumCoverPath = albumCoverPath;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        this.mAlbumName = albumName;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlbumName);
        dest.writeString(mArtist);
        dest.writeString(mAlbumCoverPath);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("mAlbumName = ");
        stringBuilder.append(mAlbumName != null ? mAlbumName + ", " : "null, ");
        stringBuilder.append("mAlbumCoverPath = ");
        stringBuilder.append(mAlbumCoverPath != null ? mAlbumCoverPath + ", " : "null, ");
        stringBuilder.append("mArtist = ");
        stringBuilder.append(mArtist != null ? mArtist : "null");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Album && this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static final Parcelable.Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[0];
        }
    };
}
