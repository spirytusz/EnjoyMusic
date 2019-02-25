package com.zspirytus.enjoymusic.db.table;

import android.os.Parcel;
import android.os.Parcelable;

import com.zspirytus.enjoymusic.db.greendao.DaoSession;
import com.zspirytus.enjoymusic.db.greendao.FolderDao;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.jointable.JoinFolderToMusic;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

@Entity
public class Folder implements Parcelable {

    // use full path hashCode as Id.
    @Id
    private Long folderId;

    private String folderDir;
    private String folderName;
    private int folderMusicCount;

    @ToMany
    @JoinEntity(
            entity = JoinFolderToMusic.class,
            sourceProperty = "folderId",
            targetProperty = "musicId"
    )
    private List<Music> mFolderMusicList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.folderId);
        dest.writeString(this.folderDir);
        dest.writeString(this.folderName);
        dest.writeInt(this.folderMusicCount);
        dest.writeTypedList(this.mFolderMusicList);
    }

    public Folder() {
    }

    protected Folder(Parcel in) {
        this.folderId = (Long) in.readValue(Long.class.getClassLoader());
        this.folderDir = in.readString();
        this.folderName = in.readString();
        this.folderMusicCount = in.readInt();
        this.mFolderMusicList = in.createTypedArrayList(Music.CREATOR);
    }

    @Generated(hash = 1670692894)
    public Folder(Long folderId, String folderDir, String folderName, int folderMusicCount) {
        this.folderId = folderId;
        this.folderDir = folderDir;
        this.folderName = folderName;
        this.folderMusicCount = folderMusicCount;
    }

    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 2091473052)
    private transient FolderDao myDao;

    @Override
    public String toString() {
        return "Folder{" +
                "folderId=" + folderId +
                ", folderDir='" + folderDir + '\'' +
                ", folderName='" + folderName + '\'' +
                ", folderMusicCount=" + folderMusicCount +
                ", mFolderMusicList=" + mFolderMusicList +
                '}';
    }

    public Long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public String getFolderDir() {
        return this.folderDir;
    }

    public void setFolderDir(String folderDir) {
        this.folderDir = folderDir;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getFolderMusicCount() {
        return this.folderMusicCount;
    }

    public void setFolderMusicCount(int folderMusicCount) {
        this.folderMusicCount = folderMusicCount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 23642457)
    public List<Music> getMFolderMusicList() {
        if (mFolderMusicList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MusicDao targetDao = daoSession.getMusicDao();
            List<Music> mFolderMusicListNew = targetDao._queryFolder_MFolderMusicList(folderId);
            synchronized (this) {
                if (mFolderMusicList == null) {
                    mFolderMusicList = mFolderMusicListNew;
                }
            }
        }
        return mFolderMusicList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1884641740)
    public synchronized void resetMFolderMusicList() {
        mFolderMusicList = null;
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
    @Generated(hash = 1822270472)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFolderDao() : null;
    }
}