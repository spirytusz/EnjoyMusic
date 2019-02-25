package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.greendao.SongListDao;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToSongList;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity
public class SongList implements Parcelable {

    @Id
    private Long songListId;
    private String songListName;
    private int musicCount;

    @ToMany
    @JoinEntity(
            entity = JoinMusicToSongList.class,
            sourceProperty = "songListId",
            targetProperty = "musicId"
    )
    private List<Music> songsOfThisSongList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.songListId);
        dest.writeString(this.songListName);
        dest.writeInt(this.musicCount);
        dest.writeTypedList(this.songsOfThisSongList);
    }

    public SongList() {
    }

    protected SongList(Parcel in) {
        this.songListId = (Long) in.readValue(Long.class.getClassLoader());
        this.songListName = in.readString();
        this.musicCount = in.readInt();
        this.songsOfThisSongList = in.createTypedArrayList(Music.CREATOR);
    }

    @Generated(hash = 811326554)
    public SongList(Long songListId, String songListName, int musicCount) {
        this.songListId = songListId;
        this.songListName = songListName;
        this.musicCount = musicCount;
    }

    public static final Parcelable.Creator<SongList> CREATOR = new Parcelable.Creator<SongList>() {
        @Override
        public SongList createFromParcel(Parcel source) {
            return new SongList(source);
        }

        @Override
        public SongList[] newArray(int size) {
            return new SongList[size];
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
    @Generated(hash = 741189411)
    private transient SongListDao myDao;

    @Override
    public String toString() {
        return "SongList{" +
                "songListId=" + songListId +
                ", songListName='" + songListName + '\'' +
                ", musicCount=" + musicCount +
                ", songsOfThisSongList=" + songsOfThisSongList +
                '}';
    }

    public Long getSongListId() {
        return this.songListId;
    }

    public void setSongListId(Long songListId) {
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
    @Generated(hash = 1326356929)
    public List<Music> getSongsOfThisSongList() {
        if (songsOfThisSongList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            List<Music> songsOfThisSongListNew = targetDao
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
