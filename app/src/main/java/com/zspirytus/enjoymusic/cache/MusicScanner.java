package com.zspirytus.enjoymusic.cache;

import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.FileUtil;
import com.zspirytus.enjoymusic.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 音乐扫描类，获取本机所有音乐。
 * Created by ZSpirytus on 2018/8/12.
 */

public class MusicScanner {

    private static final String TAG = "MusicScanner";

    private static class SingletonHolder {
        private static MusicScanner INSTANCE = new MusicScanner();
    }

    private List<Music> mAllMusicList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;
    private List<Folder> mFolderList;

    private MusicScanner() {
        mAllMusicList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();
        mFolderList = new ArrayList<>();
    }

    public static MusicScanner getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Music> getAllMusicList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mAllMusicList;
    }

    public List<Album> getAlbumList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mAlbumList;
    }

    public List<Artist> getArtistList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mArtistList;
    }

    public List<Folder> getFolderSortedMusicList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mFolderList;
    }

    private void scan() {
        List<Music> musicList = DBManager.getInstance().getDaoSession().loadAll(Music.class);
        if (musicList != null && !musicList.isEmpty()) {
            mAllMusicList = musicList;
            mAlbumList = DBManager.getInstance().getDaoSession().loadAll(Album.class);
            mArtistList = DBManager.getInstance().getDaoSession().loadAll(Artist.class);

            SparseIntArray folderDirMapIndex = new SparseIntArray();
            for (Music music : musicList) {
                String[] dir = FileUtil.getFolderNameAndFolderDir(music.getMusicFilePath());
                int index = folderDirMapIndex.get(dir[2].hashCode(), -1);
                if (index != -1) {
                    mFolderList.get(index).addMusic(music);
                } else {
                    List<Music> folderMusicList = new ArrayList<>();
                    folderMusicList.add(music);
                    Folder folder = new Folder(dir[1], dir[0], folderMusicList);
                    mFolderList.add(folder);
                    folderDirMapIndex.append(dir[2].hashCode(), mFolderList.size() - 1);
                }
            }
        } else {
            long start = System.currentTimeMillis();
            scanDirectorily();
            LogUtil.e(TAG, "usingTime = " + (System.currentTimeMillis() - start) + "ms");
        }
    }

    private void scanDirectorily() {
        scanMusic();

        // insert into my database.
        DBManager.getInstance().getDaoSession().getMusicDao().insertOrReplaceInTx(mAllMusicList);
        DBManager.getInstance().getDaoSession().getAlbumDao().insertOrReplaceInTx(mAlbumList);
        DBManager.getInstance().getDaoSession().getArtistDao().insertOrReplaceInTx(mArtistList);
    }

    private void scanMusic() {
        Map<String, ContentValues> albumMap = prepareAlbums();
        Map<String, ContentValues> artistMap = prepareArtist();
        SparseIntArray folderDirMapIndex = new SparseIntArray();
        Set<JoinAlbumToArtist> joinAlbumToArtistSet = new HashSet<>();

        final String[] musicProjection = {
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.IS_MUSIC
        };
        final String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + " != ? And "
                + MediaStore.Audio.AudioColumns.DURATION + " >= ?";
        final String[] selectionArgs = new String[]{"0", "60000"};
        Cursor cursor = MainApplication.getBackgroundContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicProjection,
                selection,
                selectionArgs,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long musicId = cursor.getLong(0);
                long albumId = cursor.getLong(1);
                long artistId = cursor.getLong(2);
                String musicFilePath = cursor.getString(3);
                String musicName = cursor.getString(4);
                String musicFileSize = cursor.getString(5);
                long duration = cursor.getLong(6);
                String musicAddDate = cursor.getString(7);
                Music music = new Music(musicId, albumId, artistId, musicFilePath, musicName, duration, musicFileSize, Long.valueOf(musicAddDate));
                mAllMusicList.add(music);

                // create Join Table & insert into Join Tables.
                if (music.getAlbumId() != 0 && music.getArtistId() != 0) {
                    JoinAlbumToArtist joinAlbumToArtist = new JoinAlbumToArtist(music.getAlbumId(), music.getArtistId());
                    joinAlbumToArtistSet.add(joinAlbumToArtist);
                }

                ContentValues albumValues = albumMap.get(String.valueOf(albumId));
                String albumName = albumValues.getAsString(MediaStore.Audio.Albums.ALBUM);
                String albumArt = albumValues.getAsString(MediaStore.Audio.Albums.ALBUM_ART);
                int numberOfSong = albumValues.getAsInteger(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
                Album album = new Album(albumId, albumName, albumArt, numberOfSong);
                mAlbumList.add(album);

                ContentValues artistValues = artistMap.get(String.valueOf(artistId));
                String artistName = artistValues.getAsString(MediaStore.Audio.Artists.ARTIST);
                int numberOfAlbum = artistValues.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
                int numberOfTrack = artistValues.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
                Artist artist = new Artist(artistId, artistName, "", numberOfAlbum, numberOfTrack);
                mArtistList.add(artist);

                // init FolderSortedList
                String[] dir = FileUtil.getFolderNameAndFolderDir(music.getMusicFilePath());
                int index = folderDirMapIndex.get(dir[2].hashCode(), -1);
                if (index != -1) {
                    mFolderList.get(index).addMusic(music);
                } else {
                    List<Music> folderMusicList = new ArrayList<>();
                    folderMusicList.add(music);
                    Folder folder = new Folder(dir[1], dir[0], folderMusicList);
                    mFolderList.add(folder);
                    folderDirMapIndex.append(dir[2].hashCode(), mFolderList.size() - 1);
                }
            }
        }
        DBManager.getInstance().getDaoSession().getJoinAlbumToArtistDao().insertOrReplaceInTx(joinAlbumToArtistSet);
        cursor.close();
    }

    private Map<String, ContentValues> prepareAlbums() {
        final String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
        };
        Cursor cursor = MainApplication.getBackgroundContext().getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        ContentQueryMap queryMap = new ContentQueryMap(
                cursor,
                MediaStore.Audio.Albums._ID,
                false,
                null
        );
        try {
            return queryMap.getRows();
        } finally {
            cursor.close();
            queryMap.close();
        }
    }

    private Map<String, ContentValues> prepareArtist() {
        final String[] projection = {
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
        };
        Cursor cursor = MainApplication.getBackgroundContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        ContentQueryMap queryMap = new ContentQueryMap(
                cursor,
                MediaStore.Audio.Artists._ID,
                false,
                null
        );
        try {
            return queryMap.getRows();
        } finally {
            cursor.close();
            queryMap.close();
        }
    }

}
