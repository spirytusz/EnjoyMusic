package com.zspirytus.enjoymusic.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.LogUtil;

public class EntityUpdator {

    public static synchronized void updateAlbum(Album album) {
        DBManager.getInstance().getDaoSession().getAlbumDao().save(album);

        /*final String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM_ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums._COUNT,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER,
                MediaStore.Audio.Albums.CONTENT_TYPE,
                MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE,
                MediaStore.Audio.Albums.FIRST_YEAR,
                MediaStore.Audio.Albums.LAST_YEAR
        };
        String where = "where " + MediaStore.Audio.Albums._ID + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(album.getAlbumId())};
        ContentResolver contentResolver = MainApplication.getBackgroundContext().getContentResolver();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        ContentValues values = new ContentValues();
        Cursor cursor = contentResolver.query(
                uri,
                projection,
                MediaStore.Audio.Albums._ID + " = ?",
                selectionArgs,
                null
        );*/
        ContentResolver contentResolver = MainApplication.getBackgroundContext().getContentResolver();
        ContentValues values = new ContentValues();
        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        int deleted = contentResolver.delete(ContentUris.withAppendedId(sArtworkUri, album.getAlbumId()), null, null);
        LogUtil.e(EntityUpdator.class.getSimpleName(), "delete = " + deleted);
        values.put("album_id", album.getAlbumId());
        values.put("_data", album.getAlbumArt());
        contentResolver.insert(sArtworkUri, values);
        /*if (cursor != null) {
            while (cursor.moveToNext()) {
                values.put(MediaStore.Audio.Albums._ID, cursor.getString(0));
                values.put(MediaStore.Audio.Albums.ALBUM_ID, cursor.getString(1));
                values.put(MediaStore.Audio.Albums.ALBUM, cursor.getString(2));
                values.put(MediaStore.Audio.Albums.ALBUM_KEY, cursor.getString(3));
                values.put(MediaStore.Audio.Albums.ALBUM_ART, album.getAlbumArt());
                values.put(MediaStore.Audio.Albums.NUMBER_OF_SONGS, cursor.getString(5));
                values.put(MediaStore.Audio.Albums._COUNT, cursor.getString(6));
                values.put(MediaStore.Audio.Albums.ARTIST, cursor.getString(7));
                values.put(MediaStore.Audio.Albums.DEFAULT_SORT_ORDER, cursor.getString(8));
                values.put(MediaStore.Audio.Albums.CONTENT_TYPE, cursor.getString(9));
                values.put(MediaStore.Audio.Albums.ENTRY_CONTENT_TYPE, cursor.getString(10));
                values.put(MediaStore.Audio.Albums.FIRST_YEAR, cursor.getString(11));
                values.put(MediaStore.Audio.Albums.LAST_YEAR, cursor.getString(12));
            }
        }
        cursor.close();
        contentResolver.delete(uri, where, selectionArgs);
        contentResolver.insert(uri, values);*/
    }

    public static synchronized void updateArtist(Artist artist) {
        DBManager.getInstance().getDaoSession().getArtistDao().save(artist);
    }
}
