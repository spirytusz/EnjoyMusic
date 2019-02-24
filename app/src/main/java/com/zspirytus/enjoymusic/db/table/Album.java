package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.zspirytus.enjoymusic.db.greendao.AlbumDao;
import com.zspirytus.enjoymusic.db.greendao.ArtistDao;
import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToAlbum;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

@Entity
public class Album implements Parcelable {

    @Id
    private long albumId;

    private String albumName;
    private String albumArt;
    private int albumSongCount;

    @ToMany
    @JoinEntity(
            entity = JoinMusicToAlbum.class,
            sourceProperty = "albumId",
            targetProperty = "musicId"
    )
    private List<Music> contentMusic;
    @ToOne
    @JoinEntity(
            entity = JoinAlbumToArtist.class,
            sourceProperty = "albumId",
            targetProperty = "artistId"
    )
    private Artist artist;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.albumId);
        dest.writeString(this.albumName);
        dest.writeString(this.albumArt);
        dest.writeInt(this.albumSongCount);
        dest.writeTypedList(this.contentMusic);
        dest.writeParcelable(this.artist, flags);
    }

    public Album() {
    }

    protected Album(Parcel in) {
        this.albumId = in.readLong();
        this.albumName = in.readString();
        this.albumArt = in.readString();
        this.albumSongCount = in.readInt();
        this.contentMusic = in.createTypedArrayList(Music.CREATOR);
        this.artist = in.readParcelable(Artist.class.getClassLoader());
    }

    @Generated(hash = 582391069)
    public Album(long albumId, String albumName, String albumArt, int albumSongCount) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumArt = albumArt;
        this.albumSongCount = albumSongCount;
    }

    public static final Parcelable.Creator<Album> CREATOR = new Parcelable.Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
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
    @Generated(hash = 172302968)
    private transient AlbumDao myDao;

    @Generated(hash = 1550754473)
    private transient boolean artist__refreshed;

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", albumArt='" + albumArt + '\'' +
                ", albumSongCount=" + albumSongCount +
                ", contentMusic=" + contentMusic +
                ", artist=" + artist +
                '}';
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return this.albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumArt() {
        return this.albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public int getAlbumSongCount() {
        return this.albumSongCount;
    }

    public void setAlbumSongCount(int albumSongCount) {
        this.albumSongCount = albumSongCount;
    }

    /** To-one relationship, resolved on first access. */
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

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 782467558)
    public Artist peakArtist() {
        return artist;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2128689463)
    public void setArtist(Artist artist) {
        synchronized (this) {
            this.artist = artist;
            artist__refreshed = true;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 622838453)
    public List<Music> getContentMusic() {
        if (contentMusic == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            List<Music> contentMusicNew = targetDao._queryAlbum_ContentMusic(albumId);
            synchronized (this) {
                if (contentMusic == null) {
                    contentMusic = contentMusicNew;
                }
            }
        }
        return contentMusic;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 581154520)
    public synchronized void resetContentMusic() {
        contentMusic = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1023911229)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlbumDao() : null;
    }
}
