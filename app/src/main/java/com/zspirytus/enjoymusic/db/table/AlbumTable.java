package com.zspirytus.enjoymusic.db.table;

import com.zspirytus.enjoymusic.db.greendao.AlbumTableDao;
import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.SongDao;
import com.zspirytus.enjoymusic.entity.Album;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class AlbumTable {

    @Id
    private long albumId;
    private String mAlbumName;
    private String mAlbumCoverPath;
    private String mArtist;
    private String mFirstYear;
    private String mLastYear;
    private int mAlbumSongCount;
    @ToMany
    @JoinEntity(
            entity = JoinAlbumToSong.class,
            sourceProperty = "albumId",
            targetProperty = "songId"
    )
    private List<Song> songs;

    public static List<AlbumTable> create(List<Album> albums) {
        List<AlbumTable> albumTables = new ArrayList<>();
        for (Album album : albums) {
            albumTables.add(AlbumTable.create(album));
        }
        return albumTables;
    }

    public static AlbumTable create(Album album) {
        return new AlbumTable(
                album.get_id(),
                album.getAlbumName(),
                album.getAlbumCoverPath(),
                album.getArtist(),
                album.getFirstYear(),
                album.getLastYear(),
                album.getAlbumSongCount()
        );
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 228675928)
    private transient AlbumTableDao myDao;

    @Generated(hash = 758293148)
    public AlbumTable(long albumId, String mAlbumName, String mAlbumCoverPath,
                      String mArtist, String mFirstYear, String mLastYear,
                      int mAlbumSongCount) {
        this.albumId = albumId;
        this.mAlbumName = mAlbumName;
        this.mAlbumCoverPath = mAlbumCoverPath;
        this.mArtist = mArtist;
        this.mFirstYear = mFirstYear;
        this.mLastYear = mLastYear;
        this.mAlbumSongCount = mAlbumSongCount;
    }

    @Generated(hash = 785807302)
    public AlbumTable() {
    }

    public long getAlbumId() {
        return this.albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getMAlbumName() {
        return this.mAlbumName;
    }

    public void setMAlbumName(String mAlbumName) {
        this.mAlbumName = mAlbumName;
    }

    public String getMAlbumCoverPath() {
        return this.mAlbumCoverPath;
    }

    public void setMAlbumCoverPath(String mAlbumCoverPath) {
        this.mAlbumCoverPath = mAlbumCoverPath;
    }

    public String getMArtist() {
        return this.mArtist;
    }

    public void setMArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public String getMFirstYear() {
        return this.mFirstYear;
    }

    public void setMFirstYear(String mFirstYear) {
        this.mFirstYear = mFirstYear;
    }

    public String getMLastYear() {
        return this.mLastYear;
    }

    public void setMLastYear(String mLastYear) {
        this.mLastYear = mLastYear;
    }

    public int getMAlbumSongCount() {
        return this.mAlbumSongCount;
    }

    public void setMAlbumSongCount(int mAlbumSongCount) {
        this.mAlbumSongCount = mAlbumSongCount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1928915309)
    public List<Song> getSongs() {
        if (songs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongDao targetDao = daoSession.getSongDao();
            List<Song> songsNew = targetDao._queryAlbumTable_Songs(albumId);
            synchronized (this) {
                if (songs == null) {
                    songs = songsNew;
                }
            }
        }
        return songs;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 432021166)
    public synchronized void resetSongs() {
        songs = null;
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
    @Generated(hash = 1724003019)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlbumTableDao() : null;
    }
}
