package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.zspirytus.enjoymusic.db.greendao.AlbumDao;
import com.zspirytus.enjoymusic.db.greendao.ArtistArtDao;
import com.zspirytus.enjoymusic.db.greendao.ArtistDao;
import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/13.
 */

@Entity
public class Artist implements Parcelable {

    @Id
    private Long artistId;

    private String artistName;
    private int numberOfAlbums;
    private int mumberOfTracks;

    @ToMany
    @JoinEntity(
            entity = JoinAlbumToArtist.class,
            sourceProperty = "artistId",
            targetProperty = "albumId"
    )
    private List<Album> albums;

    @ToOne
    private ArtistArt artistArt;


    public Artist() {
    }

    @Generated(hash = 2060216416)
    public Artist(Long artistId, String artistName, int numberOfAlbums, int mumberOfTracks) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
        this.mumberOfTracks = mumberOfTracks;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 619827925)
    private transient ArtistDao myDao;

    @Generated(hash = 1118352004)
    private transient boolean artistArt__refreshed;

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", numberOfAlbums=" + numberOfAlbums +
                ", mumberOfTracks=" + mumberOfTracks +
                '}';
    }

    public Long getArtistId() {
        return this.artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return this.artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getNumberOfAlbums() {
        return this.numberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.numberOfAlbums = numberOfAlbums;
    }

    public int getMumberOfTracks() {
        return this.mumberOfTracks;
    }

    public void setMumberOfTracks(int mumberOfTracks) {
        this.mumberOfTracks = mumberOfTracks;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1989872803)
    public ArtistArt getArtistArt() {
        if (artistArt != null || !artistArt__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ArtistArtDao targetDao = daoSession.getArtistArtDao();
            targetDao.refresh(artistArt);
            artistArt__refreshed = true;
        }
        return artistArt;
    }

    /**
     * To-one relationship, returned entity is not refreshed and may carry only the PK property.
     */
    @Generated(hash = 572947206)
    public ArtistArt peakArtistArt() {
        return artistArt;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 977824197)
    public void setArtistArt(ArtistArt artistArt) {
        synchronized (this) {
            this.artistArt = artistArt;
            artistArt__refreshed = true;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 792819456)
    public List<Album> getAlbums() {
        if (albums == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AlbumDao targetDao = daoSession.getAlbumDao();
            List<Album> albumsNew = targetDao._queryArtist_Albums(artistId);
            synchronized (this) {
                if (albums == null) {
                    albums = albumsNew;
                }
            }
        }
        return albums;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 320857755)
    public synchronized void resetAlbums() {
        albums = null;
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
    @Generated(hash = 964463518)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getArtistDao() : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.artistId);
        dest.writeString(this.artistName);
        dest.writeInt(this.numberOfAlbums);
        dest.writeInt(this.mumberOfTracks);
        dest.writeParcelable(this.artistArt, flags);
    }

    protected Artist(Parcel in) {
        this.artistId = (Long) in.readValue(Long.class.getClassLoader());
        this.artistName = in.readString();
        this.numberOfAlbums = in.readInt();
        this.mumberOfTracks = in.readInt();
        this.artistArt = in.readParcelable(ArtistArt.class.getClassLoader());
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel source) {
            return new Artist(source);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}
