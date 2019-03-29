package com.zspirytus.enjoymusic.cache;

import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;
import com.zspirytus.enjoymusic.db.table.jointable.JoinFolderToMusic;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.FileUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private List<Folder> mFolderList;

    private MusicScanner() {
        mAllMusicList = new ArrayList<>();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();
        mFolderList = new ArrayList<>();
    }

    public static MusicScanner getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized Music getMusicByUri(Uri uri) {
        final String[] musicProjection = {
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED
        };

        Cursor cursor = null;
        String schema = uri.getScheme();
        if ("file".equals(schema)) {
            final String selection = MediaStore.Audio.AudioColumns.DATA + " == ? ";
            final String[] selectionArgs = new String[]{uri.getPath()};
            cursor = MainApplication.getAppContext().getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    musicProjection,
                    selection,
                    selectionArgs,
                    null);
        } else if ("content".equals(schema)) {
            // TODO: 2019/3/27 add or remove content support.
            cursor = MainApplication.getAppContext().getContentResolver().query(
                    uri,
                    musicProjection,
                    null,
                    null,
                    null);
        }
        if (cursor != null) {
            Music music;
            if (cursor.moveToNext()) {
                long musicId = cursor.getLong(0);
                long albumId = cursor.getLong(1);
                long artistId = cursor.getLong(2);
                String musicFilePath = cursor.getString(3);
                String musicName = cursor.getString(4);
                long duration = cursor.getLong(5);
                String musicAddDate = cursor.getString(6);
                music = new Music(musicId, albumId, artistId, musicFilePath, musicName, duration, Long.valueOf(musicAddDate));
                return music;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public synchronized List<Music> getAllMusicList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mAllMusicList;
    }

    public synchronized List<Album> getAlbumList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mAlbumList;
    }

    public synchronized List<Artist> getArtistList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mArtistList;
    }

    public synchronized List<Folder> getFolderSortedMusicList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderList.isEmpty()) {
            scan();
        }
        return mFolderList;
    }

    public Music scanAddedMusic(Uri uri) {
        Cursor cursor = MainApplication.getAppContext().getContentResolver().query(
                uri,
                new String[]{
                        MediaStore.Audio.AudioColumns._ID,
                        MediaStore.Audio.AudioColumns.ALBUM_ID,
                        MediaStore.Audio.AudioColumns.ARTIST_ID,
                        MediaStore.Audio.AudioColumns.DATA,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.DATE_ADDED
                },
                null,
                null,
                null
        );
        if (cursor.moveToNext()) {
            long musicId = cursor.getLong(0);
            long albumId = cursor.getLong(1);
            long artistId = cursor.getLong(2);
            String musicFilePath = cursor.getString(3);
            String musicName = cursor.getString(4);
            long duration = cursor.getLong(5);
            String musicAddDate = cursor.getString(6);
            return new Music(musicId, albumId, artistId, musicFilePath, musicName, duration, Long.valueOf(musicAddDate));
        }
        return null;
    }

    private void scan() {
        /*List<Music> musicList = DBManager.getInstance().getDaoSession().loadAll(Music.class);
        if (musicList != null && !musicList.isEmpty()) {
            mAllMusicList = musicList;
            mAlbumList = DBManager.getInstance().getDaoSession().loadAll(Album.class);
            mArtistList = DBManager.getInstance().getDaoSession().loadAll(Artist.class);
            mFolderList = DBManager.getInstance().getDaoSession().loadAll(Folder.class);
        } else {
            scanDirectorily();
        }*/
        scanDirectorily();
    }

    private void scanDirectorily() {
        scanMusic();

        // insert into my database.
        DBManager.getInstance().getDaoSession().deleteAll(Music.class);
        DBManager.getInstance().getDaoSession().deleteAll(Album.class);
        DBManager.getInstance().getDaoSession().deleteAll(Artist.class);
        DBManager.getInstance().getDaoSession().deleteAll(Folder.class);

        DBManager.getInstance().getDaoSession().getMusicDao().insertOrReplaceInTx(mAllMusicList);
        DBManager.getInstance().getDaoSession().getAlbumDao().insertOrReplaceInTx(mAlbumList);
        DBManager.getInstance().getDaoSession().getArtistDao().insertOrReplaceInTx(mArtistList);
        DBManager.getInstance().getDaoSession().getFolderDao().insertOrReplaceInTx(mFolderList);
    }

    private void scanMusic() {
        Map<String, ContentValues> albumMap = prepareAlbums();
        Map<String, ContentValues> artistMap = prepareArtist();
        SparseIntArray array = new SparseIntArray();
        Set<JoinAlbumToArtist> joinAlbumToArtistSet = new HashSet<>();
        Set<JoinFolderToMusic> joinFolderToMusics = new HashSet<>();

        final String[] musicProjection = {
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.IS_MUSIC
        };
        final String selection = MediaStore.Audio.AudioColumns.IS_MUSIC + " != ? And "
                + MediaStore.Audio.AudioColumns.DURATION + " >= ?";
        final String[] selectionArgs = new String[]{"0", "60000"};
        Cursor cursor = MainApplication.getAppContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicProjection,
                selection,
                selectionArgs,
                null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                long musicId = cursor.getLong(0);
                long albumId = cursor.getLong(1);
                long artistId = cursor.getLong(2);
                String musicFilePath = cursor.getString(3);
                String musicName = cursor.getString(4);
                long duration = cursor.getLong(5);
                String musicAddDate = cursor.getString(6);
                Music music = new Music(musicId, albumId, artistId, musicFilePath, musicName, duration, Long.valueOf(musicAddDate));
                mAllMusicList.add(music);

                // create Join Table & insert into Join Tables.
                if (music.getAlbumId() != 0 && music.getArtistId() != 0) {
                    JoinAlbumToArtist joinAlbumToArtist = new JoinAlbumToArtist(music.getAlbumId(), music.getArtistId());
                    joinAlbumToArtistSet.add(joinAlbumToArtist);
                }

                ContentValues albumValues = albumMap.get(String.valueOf(albumId));
                String albumName = albumValues.getAsString(MediaStore.Audio.Albums.ALBUM);
                String albumArt = albumValues.getAsString(MediaStore.Audio.Albums.ALBUM_ART);
                int numberOfSong = albumValues.getAsInteger(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
                Album album = new Album(albumId, albumName, albumArt, numberOfSong);
                mAlbumList.add(album);

                ContentValues artistValues = artistMap.get(String.valueOf(artistId));
                String artistName = artistValues.getAsString(MediaStore.Audio.Artists.ARTIST);
                int numberOfAlbum = artistValues.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
                int numberOfTrack = artistValues.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
                Artist artist = new Artist(artistId, artistName, numberOfAlbum, numberOfTrack);
                mArtistList.add(artist);

                String[] dir = FileUtil.getFolderNameAndFolderDir(music.getMusicFilePath());
                String fullPath = dir[2];
                int index = array.get(fullPath.hashCode(), -1);
                if (index != -1) {
                    int oldCount = mFolderList.get(index).getFolderMusicCount();
                    int newCount = oldCount + 1;
                    mFolderList.get(index).setFolderMusicCount(newCount);
                } else {
                    String folderName = dir[0];
                    String folderDir = dir[1];
                    Folder folder = new Folder(Long.parseLong(String.valueOf(fullPath.hashCode())), folderDir, folderName, 1);
                    mFolderList.add(folder);
                    array.put(dir[2].hashCode(), mFolderList.size() - 1);
                }
                JoinFolderToMusic joinFolderToMusic = new JoinFolderToMusic();
                joinFolderToMusic.setFolderId(fullPath.hashCode());
                joinFolderToMusic.setMusicId(musicId);
                joinFolderToMusics.add(joinFolderToMusic);
            }
        }

        DBManager.getInstance().getDaoSession().getJoinAlbumToArtistDao().insertOrReplaceInTx(joinAlbumToArtistSet);
        DBManager.getInstance().getDaoSession().getJoinFolderToMusicDao().insertOrReplaceInTx(joinFolderToMusics);
        cursor.close();
    }

    private Map<String, ContentValues> prepareAlbums() {
        final String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
        };
        Cursor cursor = MainApplication.getAppContext().getContentResolver().query(
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
        try {
            return queryMap.getRows();
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
        Cursor cursor = MainApplication.getAppContext().getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
        );
        ContentQueryMap queryMap = new ContentQueryMap(
                cursor,
                MediaStore.Audio.Artists._ID,
                false,
                null
        );
        try {
            return queryMap.getRows();
        } finally {
            cursor.close();
            queryMap.close();
        }
    }

}
