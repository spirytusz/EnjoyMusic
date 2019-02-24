package com.zspirytus.enjoymusic.engine;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicMetaData;

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
        metaData.setTitle(music.getMusicName());
        metaData.setAlbum(music.getMusicAlbumName());
        metaData.setArtist(music.getMusicArtist());
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
