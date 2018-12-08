package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MusicFilter implements Parcelable {

    private String mMusicAlbum;
    private String mMusicArtist;

    public MusicFilter(String musicAlbum, String musicArtist) {
        mMusicAlbum = musicAlbum;
        mMusicArtist = musicArtist;
    }

    private MusicFilter(Parcel source) {
        mMusicAlbum = source.readString();
        mMusicArtist = source.readString();
    }

    public String getMusicAlbum() {
        return mMusicAlbum;
    }

    public String getMusicArtist() {
        return mMusicArtist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMusicAlbum);
        dest.writeString(mMusicArtist);
    }

    public static final Parcelable.Creator<MusicFilter> CREATOR = new Creator<MusicFilter>() {
        @Override
        public MusicFilter createFromParcel(Parcel source) {
            return new MusicFilter(source);
        }

        @Override
        public MusicFilter[] newArray(int size) {
            return new MusicFilter[0];
        }
    };
}
