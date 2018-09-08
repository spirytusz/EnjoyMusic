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
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 音乐缓存类，缓存当前播放音乐；获取本机所有音乐.
 * Created by ZSpirytus on 2018/8/12.
 */

public class MusicCache {

    private static final MusicCache INSTANCE = new MusicCache();
    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";

    private static Music currentPlayingMusic;
    private static List<Music> musicList;

    private MusicCache() {
        scanMusic();
        restoreCurrentPlayingMusic();
    }

    public static MusicCache getInstance() {
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
        return musicList;
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

    /*public List<MediaBrowserCompat.MediaItem> scanMusicToMediaService() {
        ArrayList<MediaBrowserCompat.MediaItem> mediaItems = new ArrayList<>();
        ContentResolver resolver = BaseActivity.getContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String thumbAlbumPath = getThumbAlbum(albumId);
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                MediaMetadataCompat mediaMetadataCompat = new MediaMetadataCompat.Builder()
                        .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, title)
                        .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                        .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeFile(thumbAlbumPath))
                        .build();
                mediaItems.add(createMediaItem(mediaMetadataCompat));
            }
            cursor.close();
        }
        return mediaItems;
    }

    private MediaBrowserCompat.MediaItem createMediaItem(MediaMetadataCompat metadata) {
        return new MediaBrowserCompat.MediaItem(
                metadata.getDescription(),
                MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        );
    }*/

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
        } else if (musicList != null && musicList.size() > 0) {
            currentPlayingMusic = musicList.get(0);
        }
    }

    public void saveCurrentPlayingMusic() {
        if (currentPlayingMusic != null) {
            SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE).edit();
            Gson gson = new Gson();
            String json = gson.toJson(currentPlayingMusic, currentPlayingMusic.getClass());
            editor.putString(CURRENT_PLAYING_MUSIC_STRING_KEY, json);
            editor.apply();
        }
    }

}
