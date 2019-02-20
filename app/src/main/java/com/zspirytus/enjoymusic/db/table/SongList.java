package com.zspirytus.enjoymusic.db.table;

import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.SongDao;
import com.zspirytus.enjoymusic.db.greendao.SongListDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity
public class SongList {

    @Id
    private long songListId;
    private String songListName;
    private int musicCount;
    @ToMany
    @JoinEntity(
            entity = JoinSongListToSong.class,
            sourceProperty = "songListId",
            targetProperty = "songId"
    )
    private List<Song> songsOfThisSongList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 741189411)
    private transient SongListDao myDao;

    @Generated(hash = 1222049496)
    public SongList(long songListId, String songListName, int musicCount) {
        this.songListId = songListId;
        this.songListName = songListName;
        this.musicCount = musicCount;
    }

    @Generated(hash = 1144407077)
    public SongList() {
    }

    public long getSongListId() {
        return this.songListId;
    }

    public void setSongListId(long songListId) {
        this.songListId = songListId;
    }

    public String getSongListName() {
        return this.songListName;
    }

    public void setSongListName(String songListName) {
        this.songListName = songListName;
    }

    public int getMusicCount() {
        return this.musicCount;
    }

    public void setMusicCount(int musicCount) {
        this.musicCount = musicCount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 489060816)
    public List<Song> getSongsOfThisSongList() {
        if (songsOfThisSongList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SongDao targetDao = daoSession.getSongDao();
            List<Song> songsOfThisSongListNew = targetDao
                    ._querySongList_SongsOfThisSongList(songListId);
            synchronized (this) {
                if (songsOfThisSongList == null) {
                    songsOfThisSongList = songsOfThisSongListNew;
                }
            }
        }
        return songsOfThisSongList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1736105235)
    public synchronized void resetSongsOfThisSongList() {
        songsOfThisSongList = null;
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
    @Generated(hash = 1874485990)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSongListDao() : null;
    }
}
