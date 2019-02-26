package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.zspirytus.enjoymusic.db.greendao.AlbumDao;
import com.zspirytus.enjoymusic.db.greendao.ArtistDao;
import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Music实体类
 * Created by ZSpirytus on 2018/8/4.
 */

@Entity
public class Music implements Parcelable {

    @Id
    private Long musicId;

    private Long albumId;
    private Long artistId;
    private String musicFilePath;
    private String musicName;
    private long musicDuration;
    private String musicFileSize;
    private long musicAddDate;

    @ToOne
    private Album album;
    @ToOne
    private Artist artist;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.musicId);
        dest.writeValue(this.albumId);
        dest.writeValue(this.artistId);
        dest.writeString(this.musicFilePath);
        dest.writeString(this.musicName);
        dest.writeLong(this.musicDuration);
        dest.writeString(this.musicFileSize);
        dest.writeLong(this.musicAddDate);
        dest.writeParcelable(this.album, flags);
        dest.writeParcelable(this.artist, flags);
    }

    public Music() {
    }

    protected Music(Parcel in) {
        this.musicId = (Long) in.readValue(Long.class.getClassLoader());
        this.albumId = (Long) in.readValue(Long.class.getClassLoader());
        this.artistId = (Long) in.readValue(Long.class.getClassLoader());
        this.musicFilePath = in.readString();
        this.musicName = in.readString();
        this.musicDuration = in.readLong();
        this.musicFileSize = in.readString();
        this.musicAddDate = in.readLong();
        this.album = in.readParcelable(Album.class.getClassLoader());
        this.artist = in.readParcelable(Artist.class.getClassLoader());
    }

    @Generated(hash = 1235278863)
    public Music(Long musicId, Long albumId, Long artistId, String musicFilePath,
                 String musicName, long musicDuration, String musicFileSize, long musicAddDate) {
        this.musicId = musicId;
        this.albumId = albumId;
        this.artistId = artistId;
        this.musicFilePath = musicFilePath;
        this.musicName = musicName;
        this.musicDuration = musicDuration;
        this.musicFileSize = musicFileSize;
        this.musicAddDate = musicAddDate;
    }

    public static final Parcelable.Creator<Music> CREATOR = new Parcelable.Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1255683360)
    private transient MusicDao myDao;

    @Generated(hash = 1449801263)
    private transient boolean album__refreshed;

    @Generated(hash = 1550754473)
    private transient boolean artist__refreshed;

    @Override
    public String toString() {
        return "Music{" +
                "musicId=" + musicId +
                ", albumId=" + albumId +
                ", artistId=" + artistId +
                ", musicFilePath='" + musicFilePath + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicDuration=" + musicDuration +
                ", musicFileSize='" + musicFileSize + '\'' +
                ", musicAddDate=" + musicAddDate +
                '}';
    }

    public Long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }

    public Long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getMusicFilePath() {
        return this.musicFilePath;
    }

    public void setMusicFilePath(String musicFilePath) {
        this.musicFilePath = musicFilePath;
    }

    public String getMusicName() {
        return this.musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public long getMusicDuration() {
        return this.musicDuration;
    }

    public void setMusicDuration(long musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getMusicFileSize() {
        return this.musicFileSize;
    }

    public void setMusicFileSize(String musicFileSize) {
        this.musicFileSize = musicFileSize;
    }

    public long getMusicAddDate() {
        return this.musicAddDate;
    }

    public void setMusicAddDate(long musicAddDate) {
        this.musicAddDate = musicAddDate;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1355616784)
    public Album getAlbum() {
        if (album != null || !album__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AlbumDao targetDao = daoSession.getAlbumDao();
            targetDao.refresh(album);
            album__refreshed = true;
        }
        return album;
    }

    /**
     * To-one relationship, returned entity is not refreshed and may carry only the PK property.
     */
    @Generated(hash = 371327305)
    public Album peakAlbum() {
        return album;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1875665110)
    public void setAlbum(Album album) {
        synchronized (this) {
            this.album = album;
            album__refreshed = true;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 88144874)
    public Artist getArtist() {
        if (artist != null || !artist__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ArtistDao targetDao = daoSession.getArtistDao();
            targetDao.refresh(artist);
            artist__refreshed = true;
        }
        return artist;
    }

    /**
     * To-one relationship, returned entity is not refreshed and may carry only the PK property.
     */
    @Generated(hash = 782467558)
    public Artist peakArtist() {
        return artist;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2128689463)
    public void setArtist(Artist artist) {
        synchronized (this) {
            this.artist = artist;
            artist__refreshed = true;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1218270154)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMusicDao() : null;
    }
}
