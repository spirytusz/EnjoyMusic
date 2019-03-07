package com.zspirytus.enjoymusic.db.table.jointable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

import java.util.Objects;

@Entity(indexes = {
        @Index(value = "folderId ASC, musicId ASC", unique = true)
})
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

    @Id
    private Long id;
    private long folderId;
    private long musicId;

    @Generated(hash = 1636047669)
    public JoinFolderToMusic(Long id, long folderId, long musicId) {
        this.id = id;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
