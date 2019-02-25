package com.zspirytus.enjoymusic.db;

import com.zspirytus.enjoymusic.db.greendao.AlbumDao;
import com.zspirytus.enjoymusic.db.greendao.ArtistDao;
import com.zspirytus.enjoymusic.db.greendao.JoinAlbumToArtistDao;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class QueryExecutor {

    private static final String TAG = "QueryExecutor";

    public static Album findAlbum(Music music) {
        if (music.peakAlbum() != null) {
            return music.peakAlbum();
        } else {
            List<Album> albumList = DBManager.getInstance().getDaoSession().queryBuilder(Album.class)
                    .where(AlbumDao.Properties.AlbumId.eq(music.getAlbumId()))
                    .list();
            Album album = albumList.get(0);
            music.setAlbum(album);
            return albumList.get(0);
        }
    }

    public static List<Music> findMusicList(Album album) {
        return filterMusicList(MusicDao.Properties.AlbumId, album.getAlbumId());
    }

    public static List<Music> findMusicList(Artist artist) {
        return filterMusicList(MusicDao.Properties.ArtistId, artist.getArtistId());
    }

    private static List<Music> filterMusicList(Property property, Object values) {
        return DBManager.getInstance().getDaoSession().queryBuilder(Music.class)
                .where(property.eq(values)).list();
    }

    public static Artist findArtist(Music music) {
        if (music.peakArtist() != null) {
            return music.peakArtist();
        } else {
            List<Artist> artistList = DBManager.getInstance().getDaoSession().queryBuilder(Artist.class)
                    .where(ArtistDao.Properties.ArtistId.eq(music.getArtistId())).list();
            Artist artist = artistList.get(0);
            music.setArtist(artist);
            return artist;
        }
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
}
