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

    public List<Music> filter(List<Music> musicList) {
        if (mMusicAlbum != "*" && mMusicArtist != "*") {
            List<Music> filterMusicList = new ArrayList<>(musicList);
            for (int i = filterMusicList.size() - 1; i >= 0; i--) {
                Music music = filterMusicList.get(i);
                if ((mMusicAlbum != null && !music.getMusicAlbumName().equals(mMusicAlbum))
                        || (mMusicArtist != null && !music.getMusicArtist().equals(mMusicArtist))) {
                    filterMusicList.remove(music);
                }
            }
            return filterMusicList;
        } else {
            return musicList;
        }
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
