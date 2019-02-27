package com.zspirytus.enjoymusic.db.table;

import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.greendao.PlayListDao;
import com.zspirytus.enjoymusic.db.table.jointable.JoinPlayListToMusic;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity
public class PlayList {

    @Id(autoincrement = true)
    private Long playListId;
    @ToMany
    @JoinEntity(
            entity = JoinPlayListToMusic.class,
            sourceProperty = "playListId",
            targetProperty = "musicId"
    )
    private List<Music> playList;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 472247056)
    private transient PlayListDao myDao;

    @Generated(hash = 1417327061)
    public PlayList(Long playListId) {
        this.playListId = playListId;
    }

    @Generated(hash = 438209239)
    public PlayList() {
    }

    public Long getPlayListId() {
        return this.playListId;
    }

    public void setPlayListId(Long playListId) {
        this.playListId = playListId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1403851724)
    public List<Music> getPlayList() {
        if (playList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            List<Music> playListNew = targetDao._queryPlayList_PlayList(playListId);
            synchronized (this) {
                if (playList == null) {
                    playList = playListNew;
                }
            }
        }
        return playList;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 640270382)
    public synchronized void resetPlayList() {
        playList = null;
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
    @Generated(hash = 469739525)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlayListDao() : null;
    }
}
