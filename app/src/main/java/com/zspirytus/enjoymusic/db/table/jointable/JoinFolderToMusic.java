package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Objects;

@Entity
public class JoinFolderToMusic {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JoinFolderToMusic that = (JoinFolderToMusic) o;
        return folderId == that.folderId &&
                musicId == that.musicId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(folderId, musicId);
    }

    private long folderId;
    private long musicId;

    @Generated(hash = 2144676369)
    public JoinFolderToMusic(long folderId, long musicId) {
        this.folderId = folderId;
        this.musicId = musicId;
    }

    @Generated(hash = 1300339030)
    public JoinFolderToMusic() {
    }

    public long getFolderId() {
        return this.folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public long getMusicId() {
        return this.musicId;
    }

    public void setMusicId(long musicId) {
        this.musicId = musicId;
    }
}
