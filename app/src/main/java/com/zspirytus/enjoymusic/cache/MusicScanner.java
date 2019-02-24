package com.zspirytus.enjoymusic.cache;

import android.content.ContentQueryMap;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToAlbum;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToArtist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
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
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderSortedMusicList.isEmpty()) {
            scan();
        }
        return mAllMusicList;
    }

    public List<Album> getAlbumList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderSortedMusicList.isEmpty()) {
            scan();
        }
        return mAlbumList;
    }

    public List<Artist> getArtistList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderSortedMusicList.isEmpty()) {
            scan();
        }
        return mArtistList;
    }

    public List<FolderSortedMusic> getFolderSortedMusicList() {
        if (mAllMusicList.isEmpty() || mAlbumList.isEmpty() || mArtistList.isEmpty() || mFolderSortedMusicList.isEmpty()) {
            scan();
        }
        return mFolderSortedMusicList;
    }

    private void scan() {
        List<Music> musicList = DBManager.getInstance().getDaoSession().loadAll(Music.class);
        if (musicList != null && !musicList.isEmpty()) {
            mAllMusicList = musicList;
            mAlbumList = DBManager.getInstance().getDaoSession().loadAll(Album.class);
            mArtistList = DBManager.getInstance().getDaoSession().loadAll(Artist.class);

            SparseIntArray folderDirMapIndex = new SparseIntArray();
            for (Music music : musicList) {
                String[] dir = FileUtil.getFolderNameAndFolderDir(music.getMusicFilePath());
                int index = folderDirMapIndex.get(dir.hashCode(), -1);
                if (index != -1) {
                    mFolderSortedMusicList.get(index).addMusic(music);
                } else {
                    List<Music> folderMusicList = new ArrayList<>();
                    folderMusicList.add(music);
                    FolderSortedMusic folderSortedMusic = new FolderSortedMusic(dir[1], dir[0], folderMusicList);
                    mFolderSortedMusicList.add(folderSortedMusic);
                    folderDirMapIndex.append(dir[2].hashCode(), mFolderSortedMusicList.size() - 1);
                }
            }
        } else {
            scanDirectorily();
        }
    }

    private void scanDirectorily() {
        prepareMusics();
        prepareAlbums();
        prepareArtist();

        SparseIntArray folderDirMapIndex = new SparseIntArray();

        Set<JoinMusicToAlbum> joinMusicToAlbumSet = new HashSet<>();
        Set<JoinMusicToArtist> joinMusicToArtistSet = new HashSet<>();
        Set<JoinAlbumToArtist> joinAlbumToArtistSet = new HashSet<>();

        for (Music music : mAllMusicList) {
            // init FolderSortedList
            String[] dir = FileUtil.getFolderNameAndFolderDir(music.getMusicFilePath());
            int index = folderDirMapIndex.get(dir.hashCode(), -1);
            if (index != -1) {
                mFolderSortedMusicList.get(index).addMusic(music);
            } else {
                List<Music> folderMusicList = new ArrayList<>();
                folderMusicList.add(music);
                FolderSortedMusic folderSortedMusic = new FolderSortedMusic(dir[1], dir[0], folderMusicList);
                mFolderSortedMusicList.add(folderSortedMusic);
                folderDirMapIndex.append(dir[2].hashCode(), mFolderSortedMusicList.size() - 1);
            }

            // create Join Table.
            JoinMusicToAlbum joinMusicToAlbum = new JoinMusicToAlbum(music.getMusicId(), music.getAlbumId());
            JoinMusicToArtist joinMusicToArtist = new JoinMusicToArtist(music.getMusicId(), music.getArtistId());
            JoinAlbumToArtist joinAlbumToArtist = new JoinAlbumToArtist(music.getAlbumId(), music.getArtistId());

            // insert into Join Tables.
            joinMusicToAlbumSet.add(joinMusicToAlbum);
            joinMusicToArtistSet.add(joinMusicToArtist);
            joinAlbumToArtistSet.add(joinAlbumToArtist);
        }

        // insert into database.
        DBManager.getInstance().getDaoSession().getJoinMusicToAlbumDao().insertOrReplaceInTx(joinMusicToAlbumSet);
        DBManager.getInstance().getDaoSession().getJoinMusicToArtistDao().insertOrReplaceInTx(joinMusicToArtistSet);
        DBManager.getInstance().getDaoSession().getJoinAlbumToArtistDao().insertOrReplaceInTx(joinAlbumToArtistSet);
    }

    private Map<String, ContentValues> prepareMusics() {
        final String[] musicProjection = {
                MediaStore.Audio.AudioColumns._ID,
                MediaStore.Audio.AudioColumns.ALBUM_ID,
                MediaStore.Audio.AudioColumns.ARTIST_ID,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED
        };
        Cursor cursor = MainApplication.getBackgroundContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicProjection,
                null,
                null,
                null);
        ContentQueryMap queryMap = new ContentQueryMap(
                cursor,
                MediaStore.Audio.Media._ID,
                false,
                null
        );
        Map<String, ContentValues> map = queryMap.getRows();
        for (String musicId : map.keySet()) {
            ContentValues values = map.get(musicId);
            long albumId = values.getAsLong(MediaStore.Audio.AudioColumns.ALBUM_ID);
            long artistId = values.getAsLong(MediaStore.Audio.AudioColumns.ARTIST_ID);
            String musicFilePath = values.getAsString(MediaStore.Audio.AudioColumns.DATA);
            String musicName = values.getAsString(MediaStore.Audio.AudioColumns.TITLE);
            long duration = values.getAsLong(MediaStore.Audio.AudioColumns.DURATION);
            String musicFileSize = values.getAsString(MediaStore.Audio.AudioColumns.SIZE);
            String musicAddDate = values.getAsString(MediaStore.Audio.AudioColumns.DATE_ADDED);
            Music music = new Music(Long.valueOf(musicId), albumId, artistId, musicFilePath, musicName, duration, musicFileSize, Long.valueOf(musicAddDate));
            mAllMusicList.add(music);
        }
        try {
            return map;
        } finally {
            cursor.close();
            queryMap.close();
        }
    }

    private Map<String, ContentValues> prepareAlbums() {
        final String[] projection = {
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
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
            int numberOfSong = values.getAsInteger(MediaStore.Audio.Albums.NUMBER_OF_SONGS);
            Album album = new Album(Long.valueOf(albumId), albumName, albumArt, numberOfSong);
            mAlbumList.add(album);
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
                null
        );
        ContentQueryMap queryMap = new ContentQueryMap(
                cursor,
                MediaStore.Audio.Artists._ID,
                false,
                null
        );
        Map<String, ContentValues> map = queryMap.getRows();
        for (String artistId : map.keySet()) {
            ContentValues values = map.get(artistId);
            String artistName = values.getAsString(MediaStore.Audio.Artists.ARTIST);
            int numberOfAlbum = values.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            int numberOfTrack = values.getAsInteger(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            Artist artist = new Artist(Long.valueOf(artistId), artistName, numberOfAlbum, numberOfTrack);
            mArtistList.add(artist);
        }
        try {
            return map;
        } finally {
            cursor.close();
            queryMap.close();
        }
    }

}
