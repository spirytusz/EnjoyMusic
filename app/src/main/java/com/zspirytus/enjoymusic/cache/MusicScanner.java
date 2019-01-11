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
            scanArtist();
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
        final String[] projection = {
                // is Music
                MediaStore.Audio.AudioColumns.IS_MUSIC,
                // Music path
                MediaStore.Audio.AudioColumns.DATA,
                // Music name
                MediaStore.Audio.AudioColumns.TITLE,
                // Music artist
                MediaStore.Audio.AudioColumns.ARTIST,
                // Music album id, the album cover can be got by this
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                // Music album
                MediaStore.Audio.AudioColumns.ALBUM,
                // Music file size
                MediaStore.Audio.AudioColumns.SIZE,
                // Music duration
                MediaStore.Audio.AudioColumns.DURATION,
                MediaStore.Audio.AudioColumns.DATE_ADDED,
        };
        ContentResolver resolver = MainApplication.getBackgroundContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic == 0) {
                    continue;
                }
                long musicDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                if (musicDuration <= 60 * 1000) {
                    continue;
                }
                String musicFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                String musicName = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
                String musicAlbumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                String musicThumbAlbumCoverPath = getThumbAlbum(albumId);
                String musicArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                String musicFileSize = Formatter.formatFileSize(MainApplication.getBackgroundContext(), cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                long musicAddDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED));
                Music itemMusic = new Music(musicFilePath, musicName, musicArtist, musicAlbumName, musicThumbAlbumCoverPath, musicDuration, musicFileSize, musicAddDate);
                if (!mAllMusicList.contains(itemMusic)) {
                    mAllMusicList.add(itemMusic);
                }
            }
            cursor.close();
        }
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

    private String getThumbAlbum(String albumId) {
        ContentResolver resolver = MainApplication.getBackgroundContext().getContentResolver();
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String thumbnail = MediaStore.Audio.Albums.ALBUM_ART;
        String id = MediaStore.Audio.Albums._ID;
        String str = null;
        Cursor cursor = resolver.query(albumUri, new String[]{thumbnail}, id + "=?", new String[]{albumId}, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex(thumbnail));
        }
        return str;
    }

}
