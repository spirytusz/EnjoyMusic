package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class CustomAlbumArt implements Parcelable {

    @Id
    private Long albumId;
    private String customAlbumArt;

    @Generated(hash = 1009503134)
    public CustomAlbumArt(Long albumId, String customAlbumArt) {
        this.albumId = albumId;
        this.customAlbumArt = customAlbumArt;
    }

    @Generated(hash = 1434492344)
    public CustomAlbumArt() {
    }

    @Override
    public String toString() {
        return "CustomAlbumArt{" +
                "albumId=" + albumId +
                ", customAlbumArt='" + customAlbumArt + '\'' +
                '}';
    }

    public Long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public String getCustomAlbumArt() {
        return this.customAlbumArt;
    }

    public void setCustomAlbumArt(String customAlbumArt) {
        this.customAlbumArt = customAlbumArt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.albumId);
        dest.writeString(this.customAlbumArt);
    }

    protected CustomAlbumArt(Parcel in) {
        this.albumId = (Long) in.readValue(Long.class.getClassLoader());
        this.customAlbumArt = in.readString();
    }

    public static final Parcelable.Creator<CustomAlbumArt> CREATOR = new Parcelable.Creator<CustomAlbumArt>() {
        @Override
        public CustomAlbumArt createFromParcel(Parcel source) {
            return new CustomAlbumArt(source);
        }

        @Override
        public CustomAlbumArt[] newArray(int size) {
            return new CustomAlbumArt[size];
        }
    };
}
