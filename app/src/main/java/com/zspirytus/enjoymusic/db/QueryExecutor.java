package com.zspirytus.enjoymusic.db;

import com.zspirytus.enjoymusic.db.greendao.JoinAlbumToArtistDao;
import com.zspirytus.enjoymusic.db.greendao.JoinFolderToMusicDao;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.greendao.PlayHistoryDao;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.db.table.CustomAlbumArt;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.PlayHistory;
import com.zspirytus.enjoymusic.db.table.PlayList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;
import com.zspirytus.enjoymusic.db.table.jointable.JoinFolderToMusic;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class QueryExecutor {

    private static final String TAG = "QueryExecutor";

    private static class Singleton {
        static QueryExecutor INSTANCE = new QueryExecutor();
    }

    private QueryExecutor() {

    }

    private Query<Album> artistFilterAlbumQuery;
    private Query<Music> folderFilterMusicQuery;

    public static List<Music> findMusicList(Album album) {
        return filterMusicList(MusicDao.Properties.AlbumId, album.getAlbumId());
    }

    public static List<Music> findMusicList(Artist artist) {
        return filterMusicList(MusicDao.Properties.ArtistId, artist.getArtistId());
    }

    public static List<Music> findMusicList(Folder folder) {
        Query<Music> query = getMusicQuery(folder).setParameter(0, folder.getFolderId());
        return query.list();
    }

    public static Album findAlbum(Music music) {
        if (music.peakAlbum() != null) {
            return music.peakAlbum();
        } else {
            Album album = DBManager.getInstance().getDaoSession().getAlbumDao().load(music.getAlbumId());
            music.setAlbum(album);
            return album;
        }
    }

    public static CustomAlbumArt findCustomAlbumArt(Album album) {
        return DBManager.getInstance().getDaoSession().getCustomAlbumArtDao().load(album.getAlbumId());
    }

    public static List<Album> findAlbumList(Artist artist) {
        Query<Album> query = getAlbumQuery(artist).setParameter(0, artist.getArtistId());
        return query.list();
    }

    public static Artist findArtist(Music music) {
        if (music.peakArtist() != null) {
            return music.peakArtist();
        } else {
            Artist artist = DBManager.getInstance().getDaoSession().getArtistDao().load(music.getArtistId());
            music.setArtist(artist);
            return artist;
        }
    }

    public static ArtistArt findArtistArt(Artist artist) {
        return DBManager.getInstance().getDaoSession().getArtistArtDao().load(artist.getArtistId());
    }

    public static Artist findArtist(Album album) {
        QueryBuilder<Artist> queryBuilder = DBManager.getInstance().getDaoSession().queryBuilder(Artist.class);
        queryBuilder.join(JoinAlbumToArtist.class, JoinAlbumToArtistDao.Properties.ArtistId)
                .where(JoinAlbumToArtistDao.Properties.AlbumId.eq(album.getAlbumId()));
        List<Artist> artistList = queryBuilder.list();
        if (!artistList.isEmpty()) {
            return artistList.get(0);
        } else {
            return null;
        }
    }

    public static List<Music> getPlayHistory(int limit) {
        List<PlayHistory> playHistories = DBManager.getInstance().getDaoSession().queryBuilder(PlayHistory.class)
                .orderAsc(PlayHistoryDao.Properties.Timestamp)
                .limit(limit)
                .list();
        List<Music> musicList = new ArrayList<>();
        for (PlayHistory playHistory : playHistories) {
            musicList.add(DBManager.getInstance().getDaoSession().getMusicDao().load(playHistory.getMusicId()));
        }
        return musicList;
    }

    public static List<Music> getPlayList() {
        List<PlayList> playLists = DBManager.getInstance().getDaoSession().loadAll(PlayList.class);
        List<Music> musicList = new ArrayList<>();
        for (PlayList playList : playLists) {
            musicList.add(DBManager.getInstance().getDaoSession().getMusicDao().load(playList.getMusicId()));
        }
        return musicList;
    }

    public static Music getLastestPlayMusic() {
        List<Music> playHistory = getPlayHistory(1);
        if (!playHistory.isEmpty()) {
            return playHistory.get(0);
        } else {
            return null;
        }
    }

    private static List<Music> filterMusicList(Property property, Object values) {
        return DBManager.getInstance().getDaoSession().queryBuilder(Music.class)
                .where(property.eq(values)).list();
    }

    private static Query<Album> getAlbumQuery(Artist artist) {
        if (Singleton.INSTANCE.artistFilterAlbumQuery == null) {
            long artistId = artist.getArtistId();
            QueryBuilder<Album> queryBuilder = DBManager.getInstance().getDaoSession().queryBuilder(Album.class);
            queryBuilder.join(JoinAlbumToArtist.class, JoinAlbumToArtistDao.Properties.AlbumId)
                    .where(JoinAlbumToArtistDao.Properties.ArtistId.eq(artistId));
            Singleton.INSTANCE.artistFilterAlbumQuery = queryBuilder.build().forCurrentThread();
        }
        return Singleton.INSTANCE.artistFilterAlbumQuery;
    }

    private static Query<Music> getMusicQuery(Folder folder) {
        if (Singleton.INSTANCE.folderFilterMusicQuery == null) {
            long folderId = folder.getFolderId();
            QueryBuilder<Music> queryBuilder = DBManager.getInstance().getDaoSession().queryBuilder(Music.class);
            queryBuilder.join(JoinFolderToMusic.class, JoinFolderToMusicDao.Properties.MusicId)
                    .where(JoinFolderToMusicDao.Properties.FolderId.eq(folderId));
            Singleton.INSTANCE.folderFilterMusicQuery = queryBuilder.build().forCurrentThread();
        }
        return Singleton.INSTANCE.folderFilterMusicQuery;
    }
}
