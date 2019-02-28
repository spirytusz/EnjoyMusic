package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class ArtistArt implements Parcelable {

    @Id
    private Long artistId;
    private String artistArt;

    @Generated(hash = 792528048)
    public ArtistArt(Long artistId, String artistArt) {
        this.artistId = artistId;
        this.artistArt = artistArt;
    }

    @Generated(hash = 1269138158)
    public ArtistArt() {
    }

    public Long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistArt() {
        return this.artistArt;
    }

    public void setArtistArt(String artistArt) {
        this.artistArt = artistArt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.artistId);
        dest.writeString(this.artistArt);
    }

    protected ArtistArt(Parcel in) {
        this.artistId = (Long) in.readValue(Long.class.getClassLoader());
        this.artistArt = in.readString();
    }

    public static final Parcelable.Creator<ArtistArt> CREATOR = new Parcelable.Creator<ArtistArt>() {
        @Override
        public ArtistArt createFromParcel(Parcel source) {
            return new ArtistArt(source);
        }

        @Override
        public ArtistArt[] newArray(int size) {
            return new ArtistArt[size];
        }
    };

    @Override
    public String toString() {
        return "ArtistArt{" +
                "artistId=" + artistId +
                ", artistArt='" + artistArt + '\'' +
                '}';
    }
}
