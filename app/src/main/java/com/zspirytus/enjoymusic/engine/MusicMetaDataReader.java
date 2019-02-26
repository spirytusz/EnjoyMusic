package com.zspirytus.enjoymusic.engine;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;

import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.listitem.MusicMetaData;

import java.io.IOException;

public class MusicMetaDataReader {

    private MediaMetadataRetriever mRetriever;
    private MediaExtractor mMediaExtractor;

    private static class Singleton {
        static MusicMetaDataReader INSTANCE = new MusicMetaDataReader();
    }

    private MusicMetaDataReader() {
    }

    public static MusicMetaDataReader getInstance() {
        return Singleton.INSTANCE;
    }

    public MusicMetaData readMetaData(Music music) {
        mRetriever = new MediaMetadataRetriever();
        mMediaExtractor = new MediaExtractor();
        mRetriever.setDataSource(music.getMusicFilePath());
        try {
            mMediaExtractor.setDataSource(music.getMusicFilePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MusicMetaData metaData = new MusicMetaData();
        Album album = QueryExecutor.findAlbum(music);
        Artist artist = QueryExecutor.findArtist(music);
        metaData.setTitle(music.getMusicName());
        metaData.setAlbum(album.getAlbumName());
        metaData.setArtist(artist.getArtistName());
        metaData.setDuration(music.getMusicDuration());
        String mime = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        String date = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        String bitrate = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        MediaFormat mediaFormat = mMediaExtractor.getTrackFormat(0);
        int sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        metaData.setSampleRate(sampleRate);
        metaData.setMime(mime);
        metaData.setDate(date);
        metaData.setBitrate(bitrate);
        return metaData;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (mRetriever != null) {
            mRetriever.release();
        }
    }
}
