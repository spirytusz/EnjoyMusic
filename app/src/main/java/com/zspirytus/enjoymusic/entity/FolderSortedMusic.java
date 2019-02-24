package com.zspirytus.enjoymusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.zspirytus.enjoymusic.db.table.Music;

import java.util.ArrayList;
import java.util.List;

public class FolderSortedMusic implements Parcelable {

    private String mParentFolderDir;
    private String mFolderName;
    private List<Music> mFolderMusicList;
    private int mFolderMusicCount;

    public FolderSortedMusic(String parentFolderDir, String folderName, List<Music> folderMusicList) {
        mParentFolderDir = parentFolderDir;
        mFolderName = folderName;
        mFolderMusicList = folderMusicList;
        if (mFolderMusicList != null)
            mFolderMusicCount = mFolderMusicList.size();
    }

    private FolderSortedMusic(Parcel source) {
        mParentFolderDir = source.readString();
        mFolderName = source.readString();
        mFolderMusicList = new ArrayList<>();
        source.readTypedList(mFolderMusicList, Music.CREATOR);
        mFolderMusicCount = source.readInt();
    }

    public void addMusic(Music music) {
        mFolderMusicList.add(music);
        mFolderMusicCount++;
    }

    public String getParentFolderDir() {
        return mParentFolderDir;
    }

    public String getFolderName() {
        return mFolderName;
    }

    public List<Music> getFolderMusicList() {
        return mFolderMusicList;
    }

    public int getFolderMusicCount() {
        return mFolderMusicCount;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof FolderSortedMusic && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        stringBuilder.append("parentFolderDir = ");
        stringBuilder.append(mParentFolderDir != null ? mParentFolderDir + ", " : "null, ");
        stringBuilder.append("folderName = ");
        stringBuilder.append(mFolderName != null ? mFolderName + ", " : "null, ");
        stringBuilder.append("folderMusicList = ");
        stringBuilder.append(mFolderMusicList != null ? mFolderMusicList.toString() + ", " : "null, ");
        stringBuilder.append("folderMusicCount = ");
        stringBuilder.append(mFolderMusicCount);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mParentFolderDir);
        dest.writeString(mFolderName);
        dest.writeTypedList(mFolderMusicList);
        dest.writeInt(mFolderMusicCount);
    }

    public static final Parcelable.Creator<FolderSortedMusic> CREATOR = new Creator<FolderSortedMusic>() {
        @Override
        public FolderSortedMusic createFromParcel(Parcel source) {
            return new FolderSortedMusic(source);
        }

        @Override
        public FolderSortedMusic[] newArray(int size) {
            return new FolderSortedMusic[0];
        }
    };
}