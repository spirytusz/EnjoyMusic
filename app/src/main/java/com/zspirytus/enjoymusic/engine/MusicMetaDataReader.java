package com.zspirytus.enjoymusic.engine;

import android.media.MediaMetadataRetriever;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicMetaData;

public class MusicMetaDataReader {

    private MediaMetadataRetriever mRetriever;

    private static class Singleton {
        static MusicMetaDataReader INSTANCE = new MusicMetaDataReader();
    }

    private MusicMetaDataReader() {
        mRetriever = new MediaMetadataRetriever();
    }

    public static MusicMetaDataReader getInstance() {
        return Singleton.INSTANCE;
    }

    public MusicMetaData readMetaData(Music music) {
        mRetriever.setDataSource(music.getMusicFilePath());
        MusicMetaData metaData = new MusicMetaData();
        metaData.setTitle(music.getMusicName());
        metaData.setAlbum(music.getMusicAlbumName());
        metaData.setArtist(music.getMusicArtist());
        metaData.setDuration(music.getMusicDuration());
        String mime = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
        String date = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        String bitrate = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
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
