package com.zspirytus.enjoymusic.db.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

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
}
