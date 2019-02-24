package com.zspirytus.enjoymusic.db;

import com.zspirytus.enjoymusic.db.greendao.AlbumDao;
import com.zspirytus.enjoymusic.db.greendao.ArtistDao;
import com.zspirytus.enjoymusic.db.greendao.JoinAlbumToArtistDao;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.jointable.JoinAlbumToArtist;
import com.zspirytus.enjoymusic.utils.LogUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class QueryExecutor {

    private static final String TAG = "QueryExecutor";

    public static Album findAlbum(Music music) {
        LogUtil.e(TAG, "music = " + music.getMusicName());
        List<Album> albumList = DBManager.getInstance().getDaoSession().queryBuilder(Album.class)
                .where(AlbumDao.Properties.AlbumId.eq(music.getAlbumId()))
                .list();
        return albumList.get(0);
    }

    public static Artist findArtist(Music music) {
        List<Artist> artistList = DBManager.getInstance().getDaoSession().queryBuilder(Artist.class)
                .where(ArtistDao.Properties.ArtistId.eq(music.getArtistId())).list();
        return artistList.get(0);
    }

    public static Artist findArtist(Album album) {
        LogUtil.e(TAG, "album = " + album.getAlbumName() + "\talbumId = " + album.getAlbumId());
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
