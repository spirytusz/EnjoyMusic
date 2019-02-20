package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MusicFilter implements Parcelable {

    public static MusicFilter NO_FILTER = new MusicFilter("*", "*");

    private String mMusicAlbum;
    private String mMusicArtist;

    public MusicFilter(String musicAlbum, String musicArtist) {
        if (musicAlbum != null) {
            mMusicAlbum = musicAlbum;
        } else {
            mMusicAlbum = "";
        }
        if (musicArtist != null) {
            mMusicArtist = musicArtist;
        } else {
            mMusicArtist = "";
        }
    }

    private MusicFilter(Parcel source) {
        mMusicAlbum = source.readString();
        mMusicArtist = source.readString();
    }

    public String getAlbum() {
        return mMusicAlbum;
    }

    public String getArtist() {
        return mMusicArtist;
    }

    public List<Music> filter(List<Music> musicList) {
        if (!mMusicAlbum.equals("*") && !mMusicArtist.equals("*")) {
            List<Music> filterMusicList = new ArrayList<>(musicList);
            for (int i = filterMusicList.size() - 1; i >= 0; i--) {
                Music music = filterMusicList.get(i);
                boolean isMatchAlbum = !mMusicAlbum.isEmpty() && !music.getMusicAlbumName().equals(mMusicAlbum);
                boolean isMatchArtist = !mMusicArtist.isEmpty() && !music.getMusicArtist().equals(mMusicArtist);
                if (isMatchAlbum || isMatchArtist) {
                    filterMusicList.remove(music);
                }
            }
            return filterMusicList;
        } else {
            return musicList;
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (mMusicAlbum != null) {
            stringBuilder.append("mMusicAlbum = " + mMusicAlbum + ", ");
        } else {
            stringBuilder.append("mMusicAlbum = null, ");
        }
        if (mMusicArtist != null) {
            stringBuilder.append("mMusicArtist = " + mMusicArtist);
        } else {
            stringBuilder.append("mMusicArtist = null");
        }
        return "[" + stringBuilder.toString() + "]";
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
