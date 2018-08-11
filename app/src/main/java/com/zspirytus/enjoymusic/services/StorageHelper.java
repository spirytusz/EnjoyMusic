package com.zspirytus.enjoymusic.services;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;

import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.view.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ZSpirytus on 2018/8/10.
 */

public class StorageHelper {

    private StorageHelper() {
        throw new AssertionError("must not get class: " + this.getClass().getSimpleName() + " Instance!");
    }

    public static List<Music> scanMusic() {
        List<Music> musicList = new ArrayList<>();
        SimpleDateFormat sim = new SimpleDateFormat("mm:ss", Locale.CHINA);
        ContentResolver resolver = BaseActivity.getContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String thumbAlbum = getThumbAlbum(albumId);
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String duration = sim.format(time);
            String size = Formatter.formatFileSize(BaseActivity.getContext(), cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            musicList.add(new Music(path, name, artist, thumbAlbum, null, duration, size));
        }
        return musicList;
    }

    private static String getThumbAlbum(String album_id) {
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
}
