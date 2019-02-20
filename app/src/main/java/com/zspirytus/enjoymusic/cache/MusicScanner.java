package com.zspirytus.enjoymusic.cache;

import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        return mAlbumList;
    }

    public List<Artist> getArtistList() {
        return mArtistList;
    }

    public List<FolderSortedMusic> getFolderSortedMusicList() {
        return mFolderSortedMusicList;
    }

    private void scanMusic() {
        SparseIntArray indexMemory = new SparseIntArray();
        Map<String, ContentValues> albumQueryMap = prepareAlbums();
        Map<String, ContentValues> artistQueryMap = prepareArtist();
        final String[] musicProjection = {
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
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
                // scan item music
                long id = musicCursor.getLong(0);
                String musicFilePath = musicCursor.getString(1);
                String musicName = musicCursor.getString(2);
                String musicArtist = musicCursor.getString(3);
                String musicAlbumName = musicCursor.getString(4);
                String albumId = musicCursor.getString(5);
                String coverPath = albumQueryMap.get(albumId).getAsString(MediaStore.Audio.Albums.ALBUM_ART);
                String musicFileSize = Formatter.formatFileSize(MainApplication.getBackgroundContext(), musicCursor.getLong(6));
                long musicDuration = musicCursor.getLong(7);
                long musicAddDate = musicCursor.getLong(8);
                Music itemMusic = new Music(id, musicFilePath, musicName, musicArtist, musicAlbumName, coverPath, musicDuration, musicFileSize, musicAddDate);
                mAllMusicList.add(itemMusic);

                // add to folder sorted music list
                int hash = FileUtil.getParent(musicFilePath).hashCode();
                int findIndex = indexMemory.get(hash, -1);
                if (findIndex == -1) {
                    String[] strings = FileUtil.getParentFileNameAndDir(musicFilePath);
                    String fileName = strings[0];
                    String parentDir = strings[1];
                    List<Music> musicList = new ArrayList<>();
                    musicList.add(itemMusic);
                    FolderSortedMusic folderSortedMusic = new FolderSortedMusic(fileName, parentDir, musicList);
                    mFolderSortedMusicList.add(folderSortedMusic);
                    indexMemory.put(hash, mFolderSortedMusicList.size());
                } else {
                    mFolderSortedMusicList.get(findIndex - 1).getFolderMusicList().add(itemMusic);
                }
            }
            musicCursor.close();
        }
    }

    private Map<String, ContentValues> prepareAlbums() {
        final String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.LAST_YEAR,
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
        Map<String, ContentValues> map = queryMap.getRows();
        for (String albumId : map.keySet()) {
            ContentValues values = map.get(albumId);
            String albumName = values.getAsString(MediaStore.Audio.Albums.ALBUM);
            String albumArt = values.getAsString(MediaStore.Audio.Albums.ALBUM_ART);
            String artist = values.getAsString(MediaStore.Audio.Albums.ARTIST);
            String firstYear = values.getAsString(MediaStore.Audio.Albums.FIRST_YEAR);
            String lastYear = values.getAsString(MediaStore.Audio.Albums.LAST_YEAR);
            int numberOfSongs = values.getAsInteger(MediaStore.Audio.Artists.Albums.NUMBER_OF_SONGS);
            Album item = new Album(albumName, albumArt, artist, firstYear, lastYear, numberOfSongs);
            mAlbumList.add(item);
        }
        try {
            return map;
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
                null);
        ContentQueryMap queryMap = new ContentQueryMap(
                cursor,
                MediaStore.Audio.Artists._ID,
                false,
                null
        );
        Map<String, ContentValues> map = queryMap.getRows();
        for (String artistId : map.keySet()) {
            ContentValues values = map.get(artistId);
            String artist = values.getAsString(MediaStore.Audio.Artists.ARTIST);
            int numberOfAlbums = values.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int numberOfTracks = values.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            Artist item = new Artist(artist, numberOfAlbums, numberOfTracks);
            mArtistList.add(item);
        }
        try {
            return map;
        } finally {
            cursor.close();
            queryMap.close();
        }
    }

}
