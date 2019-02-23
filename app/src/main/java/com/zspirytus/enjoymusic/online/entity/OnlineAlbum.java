
package com.zspirytus.enjoymusic.online.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class OnlineAlbum {

    @Expose
    private Long albumID;
    @Expose
    private String albumMID;
    @Expose
    private String albumName;
    @SerializedName("albumName_hilight")
    private String albumNameHilight;
    @Expose
    private String albumPic;
    @SerializedName("catch_song")
    private String catchSong;
    @Expose
    private String docid;
    @Expose
    private String publicTime;
    @Expose
    private Long singerID;
    @SerializedName("singer_list")
    private List<SingerList> singerList;
    @Expose
    private String singerMID;
    @Expose
    private String singerName;
    @SerializedName("singerName_hilight")
    private String singerNameHilight;
    @SerializedName("song_count")
    private Long songCount;
    @Expose
    private Long type;

    public Long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(Long albumID) {
        this.albumID = albumID;
    }

    public String getAlbumMID() {
        return albumMID;
    }

    public void setAlbumMID(String albumMID) {
        this.albumMID = albumMID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumNameHilight() {
        return albumNameHilight;
    }

    public void setAlbumNameHilight(String albumNameHilight) {
        this.albumNameHilight = albumNameHilight;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public String getCatchSong() {
        return catchSong;
    }

    public void setCatchSong(String catchSong) {
        this.catchSong = catchSong;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public Long getSingerID() {
        return singerID;
    }

    public void setSingerID(Long singerID) {
        this.singerID = singerID;
    }

    public List<SingerList> getSingerList() {
        return singerList;
    }

    public void setSingerList(List<SingerList> singerList) {
        this.singerList = singerList;
    }

    public String getSingerMID() {
        return singerMID;
    }

    public void setSingerMID(String singerMID) {
        this.singerMID = singerMID;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerNameHilight() {
        return singerNameHilight;
    }

    public void setSingerNameHilight(String singerNameHilight) {
        this.singerNameHilight = singerNameHilight;
    }

    public Long getSongCount() {
        return songCount;
    }

    public void setSongCount(Long songCount) {
        this.songCount = songCount;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

}
