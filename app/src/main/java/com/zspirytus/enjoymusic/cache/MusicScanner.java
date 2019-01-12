package com.zspirytus.enjoymusic.cache;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.LogUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private List<FolderSortedMusic> mFolderSortedMusicList;

    private MusicScanner() {
        mAllMusicList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();
        mFolderSortedMusicList = new ArrayList<>();
    }

    public static MusicScanner getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Music> getAllMusicList() {
        if (mAllMusicList.isEmpty()) {
            scanMusic();
        }
        return mAllMusicList;
    }

    public List<Album> getAlbumList() {
        if (mAlbumList.isEmpty()) {
            scanAlbum();
        }
        return mAlbumList;
    }

    public List<Artist> getArtistList() {
        if (mArtistList.isEmpty()) {
            scanArtist();
        }
        return mArtistList;
    }

    public List<FolderSortedMusic> getFolderSortedMusicList() {
        if (mFolderSortedMusicList.isEmpty()) {
            sortedMusicByFolder();
        }
        return mFolderSortedMusicList;
    }

    private void scanMusic() {
        long start = System.currentTimeMillis();
        final String[] musicProjection = {
                // Music path
                MediaStore.Audio.AudioColumns.DATA,
                // Music name
                MediaStore.Audio.Media.TITLE,
                // Music artist
                MediaStore.Audio.Media.ARTIST,
                // Music album
                MediaStore.Audio.Media.ALBUM,
                // Music album id
                MediaStore.Audio.Media.ALBUM_ID,
                // Music file size
                MediaStore.Audio.Media.SIZE,
                // Music duration
                MediaStore.Audio.Media.DURATION,
                // Music add date
                MediaStore.Audio.Media.DATE_ADDED
        };
        final String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + " != ? And "
                + MediaStore.Audio.AudioColumns.DURATION + " >= ?";
        final String[] selectionArgs = new String[]{"0", "60000"};
        Cursor musicCursor = MainApplication.getBackgroundContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicProjection,
                selection,
                selectionArgs,
                null
        );
        if (musicCursor != null) {
            while (musicCursor.moveToNext()) {
                String musicFilePath = musicCursor.getString(0);
                String musicName = musicCursor.getString(1);
                String musicArtist = musicCursor.getString(2);
                String musicAlbumName = musicCursor.getString(3);
                String albumId = musicCursor.getString(4);
                String coverPath = getThumbAlbum(albumId);
                String musicFileSize = Formatter.formatFileSize(MainApplication.getBackgroundContext(), musicCursor.getLong(5));
                long musicDuration = musicCursor.getLong(6);
                long musicAddDate = musicCursor.getLong(7);
                Music itemMusic = new Music(musicFilePath, musicName, musicArtist, musicAlbumName, coverPath, musicDuration, musicFileSize, musicAddDate);
                mAllMusicList.add(itemMusic);
            }
            musicCursor.close();
        }
        LogUtil.e(TAG, "Scan Used Time = " + (System.currentTimeMillis() - start) + "ms With ResultSet Size = " + mAllMusicList.size());
    }

    private String getThumbAlbum(String albumId) {
        ContentResolver resolver = MainApplication.getBackgroundContext().getContentResolver();
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String id = MediaStore.Audio.Albums._ID;
        String[] selection = new String[]{MediaStore.Audio.Albums.ALBUM_ART};
        String[] selectionArgs = new String[]{albumId};
        Cursor cursor = resolver.query(albumUri, selection, id + "=?", selectionArgs, null);
        if (cursor != null && cursor.moveToNext()) {
            try {
                return cursor.getString(0);
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    private void scanAlbum() {
        final String[] projection = {
                MediaStore.Audio.AlbumColumns.ALBUM,
                MediaStore.Audio.AlbumColumns.ALBUM_ART,
                MediaStore.Audio.AlbumColumns.ARTIST,
                MediaStore.Audio.AlbumColumns.FIRST_YEAR,
                MediaStore.Audio.AlbumColumns.LAST_YEAR,
                MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS,
        };
        ContentResolver resolver = MainApplication.getBackgroundContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String albumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM));
                String albumCoverFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ART));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ARTIST));
                String firstYear = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.FIRST_YEAR));
                String lastYear = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.LAST_YEAR));
                int numberOfSong = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS));
                Album album = new Album(albumName, albumCoverFilePath, artist, firstYear, lastYear, numberOfSong);
                if (!mAlbumList.contains(album)) {
                    mAlbumList.add(album);
                }

            }
        }
        cursor.close();
    }

    private void scanArtist() {
        final String[] projection = {
                MediaStore.Audio.ArtistColumns.ARTIST,
                MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS,
                MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS,
        };
        ContentResolver resolver = MainApplication.getBackgroundContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String artistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.ARTIST));
                int numberOfAlbums = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_ALBUMS));
                int numberOfTracks = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS));
                Artist artist = new Artist(artistName, numberOfAlbums, numberOfTracks);
                if (!mArtistList.contains(artist)) {
                    mArtistList.add(artist);
                }
            }

        }
        cursor.close();
    }

    private void sortedMusicByFolder() {
        SparseIntArray indexMemory = new SparseIntArray();
        for (Music music : getAllMusicList()) {
            File file = new File(music.getMusicFilePath());
            String parentDir = file.getParentFile().getParentFile().getPath();
            int findIndex = indexMemory.get(parentDir.hashCode());
            if (findIndex <= 0) {
                List<Music> musicList = new ArrayList<>();
                musicList.add(music);
                FolderSortedMusic folderSortedMusic = new FolderSortedMusic(file.getParentFile().getName(), file.getParentFile().getParentFile().getPath(), musicList);
                mFolderSortedMusicList.add(folderSortedMusic);
                indexMemory.put(parentDir.hashCode(), mFolderSortedMusicList.size());
            } else {
                mFolderSortedMusicList.get(findIndex - 1).getFolderMusicList().add(music);
            }
        }
    }

}
