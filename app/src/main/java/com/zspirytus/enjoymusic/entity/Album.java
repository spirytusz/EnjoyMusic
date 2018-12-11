package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class Album implements Parcelable {

    private String mAlbumName;
    private String mAlbumCoverPath;
    private String mArtist;
    private String mFirstYear;
    private String mLastYear;
    private int mAlbumSongCount;

    public Album(String albumName, String albumCoverPath, String artist, String firstYear, String lastYear, int albumSongCount) {
        mAlbumName = albumName;
        mAlbumCoverPath = albumCoverPath;
        mArtist = artist;
        mFirstYear = firstYear;
        mLastYear = lastYear;
        mAlbumSongCount = albumSongCount;
    }

    private Album(Parcel source) {
        mAlbumName = source.readString();
        mAlbumCoverPath = source.readString();
        mArtist = source.readString();
        mFirstYear = source.readString();
        mLastYear = source.readString();
        mAlbumSongCount = source.readInt();
    }

    public String getAlbumCoverPath() {
        return mAlbumCoverPath;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public String getArtist() {
        return mArtist;
    }

    public int getAlbumSongCount() {
        return mAlbumSongCount;
    }

    public String getFirstYear() {
        return mFirstYear;
    }

    public String getLastYear() {
        return mLastYear;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlbumName);
        dest.writeString(mAlbumCoverPath);
        dest.writeString(mArtist);
        dest.writeString(mFirstYear);
        dest.writeString(mLastYear);
        dest.writeInt(mAlbumSongCount);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("albumName = ");
        stringBuilder.append(mAlbumName != null ? mAlbumName + ", " : "null, ");
        stringBuilder.append("albumCoverPath = ");
        stringBuilder.append(mAlbumCoverPath != null ? mAlbumCoverPath + ", " : "null, ");
        stringBuilder.append("artist = ");
        stringBuilder.append(mArtist != null ? mArtist + ", " : "null, ");
        stringBuilder.append("firstYear = ");
        stringBuilder.append(mFirstYear != null ? mFirstYear + ", " : "null, ");
        stringBuilder.append("lastYear = ");
        stringBuilder.append(mLastYear != null ? mLastYear + ", " : "null, ");
        stringBuilder.append("albumSongCount = " + mAlbumSongCount);
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
