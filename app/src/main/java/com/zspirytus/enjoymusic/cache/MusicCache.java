package com.zspirytus.enjoymusic.cache;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.engine.MusicPlayedHistoryProvider;
import com.zspirytus.enjoymusic.entity.Music;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐缓存类，缓存当前播放音乐；获取本机所有音乐.
 * Created by ZSpirytus on 2018/8/12.
 */

public class MusicCache {

    private static MusicCache INSTANCE;
    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";

    private Music currentPlayingMusic;
    private List<Music> musicList;

    private MusicCache() {
        musicList = new ArrayList<>();
    }

    public static MusicCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MusicCache();
        }
        return INSTANCE;
    }

    public void setCurrentPlayingMusic(Music music) {
        currentPlayingMusic = music;
        MusicPlayedHistoryProvider.getInstance().put(music);
    }

    public Music getCurrentPlayingMusic() {
        return currentPlayingMusic;
    }

    public List<Music> getMusicList() {
        if (musicList == null || musicList.size() == 0) {
            scanMusic();
        }
        return musicList;
    }

    private void scanMusic() {
        final String[] projection = {
                // is Music
                MediaStore.Audio.AudioColumns.IS_MUSIC,
                // music name
                MediaStore.Audio.AudioColumns.TITLE,
                // music artist
                MediaStore.Audio.AudioColumns.ARTIST,
                // music album id, the album cover can be got by this
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                // music album
                MediaStore.Audio.AudioColumns.ALBUM,
                // music file size
                MediaStore.Audio.AudioColumns.SIZE,
                // music duration
                MediaStore.Audio.AudioColumns.DURATION
        };
        ContentResolver resolver = MyApplication.getGlobalContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
                if (isMusic == 0) {
                    // if the file is not music, continues.
                    continue;
                }
                long musicDuration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                if (musicDuration <= 60 * 1000) {
                    // if music duration less than 1 min, continues
                    continue;
                }
                String musicFilePath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
                String musicName = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.AudioColumns.TITLE));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));
                String musicAlbumName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                String musicThumbAlbumCoverPath = getThumbAlbum(albumId);
                String musicArtist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST));
                String musicFileSize = Formatter.formatFileSize(MyApplication.getGlobalContext(), cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                musicList.add(new Music(musicFilePath, musicName, musicArtist, musicAlbumName, musicThumbAlbumCoverPath, musicDuration, musicFileSize));
            }
            cursor.close();
        }
        restoreCurrentPlayingMusic();
    }

    private String getThumbAlbum(String album_id) {
        ContentResolver resolver = MyApplication.getGlobalContext().getContentResolver();
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String thumbnail = MediaStore.Audio.Albums.ALBUM_ART;
        String id = MediaStore.Audio.Albums._ID;
        String str = null;
        Cursor cursor = resolver.query(albumUri, new String[]{thumbnail}, id + "=?", new String[]{album_id}, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex(thumbnail));
        }
        return str;
    }

    private void restoreCurrentPlayingMusic() {
        SharedPreferences pref = MyApplication.getGlobalContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_STRING_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Music music = gson.fromJson(json, Music.class);
            if (music != null) {
                File file = new File(music.getMusicFilePath());
                if (file.exists()) {
                    currentPlayingMusic = music;
                }
            }
        } else if (musicList != null && musicList.size() > 0) {
            currentPlayingMusic = musicList.get(0);
        }
    }

    public void saveCurrentPlayingMusic() {
        if (currentPlayingMusic != null) {
            SharedPreferences.Editor editor = MyApplication.getGlobalContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(currentPlayingMusic, currentPlayingMusic.getClass());
            editor.putString(CURRENT_PLAYING_MUSIC_STRING_KEY, json);
            editor.apply();
        }
    }

}
