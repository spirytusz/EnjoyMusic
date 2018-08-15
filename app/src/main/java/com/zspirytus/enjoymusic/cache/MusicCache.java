package com.zspirytus.enjoymusic.cache;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.google.gson.Gson;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐缓存类，缓存当前播放音乐、历史播放记录；获取本机所有音乐等
 * Created by ZSpirytus on 2018/8/12.
 */

public class MusicCache {

    public static final int MODE_ORDER = 1;
    public static final int MODE_RANDOM = 2;

    private static final MusicCache INSTANCE = new MusicCache();
    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";

    private static Music currentPlayingMusic;
    private static List<Music> musicList;

    private MusicCache() {
        restoreCurrentPlayingMusic();
    }

    public static MusicCache getInstance() {
        return INSTANCE;
    }

    public void saveCurrentPlayingMusic(Music currentPlayingMusic) {
        if (currentPlayingMusic != null) {
            SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(currentPlayingMusic, currentPlayingMusic.getClass());
            editor.putString(CURRENT_PLAYING_MUSIC_STRING_KEY, json);
            editor.commit();
        }
    }

    public void setCurrentPlayingMusic(Music music) {
        currentPlayingMusic = music;
    }

    public Music getCurrentPlayingMusic() {
        return currentPlayingMusic;
    }

    public List<Music> getMusicList() {
        if (musicList == null) {
            scanMusic();
        }
        return musicList;
    }

    public Music getPreviousMusic(int type) {
        int previousPosition = (getCurrentPlayingMusicPosition() - 1) % musicList.size();
        return musicList.get(previousPosition);
    }

    public Music getNextMusic(int type) {
        int previousPosition = (getCurrentPlayingMusicPosition() + 1) % musicList.size();
        return musicList.get(previousPosition);
    }

    private int getCurrentPlayingMusicPosition() {
        return musicList.indexOf(currentPlayingMusic);
    }

    private void scanMusic() {
        musicList = new ArrayList<>();
        ContentResolver resolver = BaseActivity.getContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                String name = cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.TITLE));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String thumbAlbum = getThumbAlbum(albumId);
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String size = Formatter.formatFileSize(BaseActivity.getContext(), cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                musicList.add(new Music(path, name, artist, thumbAlbum, duration, size));
            }
            cursor.close();
        }
    }

    private String getThumbAlbum(String album_id) {
        ContentResolver resolver = BaseActivity.getContext().getContentResolver();
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
        SharedPreferences pref = BaseActivity.getContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_STRING_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Music music = gson.fromJson(json, Music.class);
            if (music != null) {
                File file = new File(music.getPath());
                if (file.exists()) {
                    currentPlayingMusic = music;
                }
            }
        }
    }

}
