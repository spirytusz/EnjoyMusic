package com.zspirytus.enjoymusic.cache;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.SparseArray;

import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * 音乐缓存类，缓存当前播放音乐；获取本机所有音乐.
 * Created by ZSpirytus on 2018/8/12.
 */

public class AllMusicCache {

    private static class SingletonHolder {
        private static AllMusicCache INSTANCE = new AllMusicCache();
    }

    private List<Music> mAllMusicList;
    private List<Album> mAlbumList;
    private List<Artist> mArtistList;

    private SparseArray<Integer> mArtistToIndexMapper;

    private AllMusicCache() {
        mAllMusicList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();
        mArtistToIndexMapper = new SparseArray();
    }

    public static AllMusicCache getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public List<Music> getAllMusicList() {
        if (mAllMusicList == null || mAllMusicList.isEmpty()) {
            scanMusic();
        }
        return mAllMusicList;
    }

    public List<Album> getAlbumList() {
        if (mAllMusicList.isEmpty()) {
            getAllMusicList();
        }
        for (Music music : mAllMusicList) {
            Album album = new Album(music.getMusicAlbumName(), music.getMusicThumbAlbumCoverPath(), music.getMusicArtist());
            if (!mAlbumList.contains(album)) {
                mAlbumList.add(new Album(music.getMusicAlbumName(), music.getMusicThumbAlbumCoverPath(), music.getMusicArtist()));
            }
        }
        return mAlbumList;
    }

    public List<Artist> getArtistList() {
        if (mAllMusicList.isEmpty()) {
            getAllMusicList();
        }
        for (Music music : mAllMusicList) {
            Artist artist = new Artist(music.getMusicArtist());
            if (mArtistList.contains(artist)) {
                int index = mArtistToIndexMapper.get(artist.hashCode());
                mArtistList.get(index).increaseMusicCount();
            } else {
                mArtistList.add(artist);
                mArtistToIndexMapper.put(artist.hashCode(), mArtistList.size() - 1);
            }
        }
        return mArtistList;
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
                MediaStore.Audio.AudioColumns.DURATION
        };
        ContentResolver resolver = MyApplication.getGlobalContext().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null);
        if (cursor != null) {
            synchronized (this) {
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
                    String musicFileSize = Formatter.formatFileSize(MyApplication.getGlobalContext(), cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                    Music itemMusic = new Music(musicFilePath, musicName, musicArtist, musicAlbumName, musicThumbAlbumCoverPath, musicDuration, musicFileSize);
                    if (!mAllMusicList.contains(itemMusic)) {
                        mAllMusicList.add(itemMusic);
                    }
                }
                cursor.close();
            }
        }
        CurrentPlayingMusicCache.getInstance().restoreCurrentPlayingMusic();
        MusicPlayOrderManager.getInstance().setPlayList(mAllMusicList);
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

}
