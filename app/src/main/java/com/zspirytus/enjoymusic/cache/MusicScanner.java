package com.zspirytus.enjoymusic.cache;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.global.MainApplication;

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

    private MusicScanner() {
        mAllMusicList = new ArrayList<>();
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
