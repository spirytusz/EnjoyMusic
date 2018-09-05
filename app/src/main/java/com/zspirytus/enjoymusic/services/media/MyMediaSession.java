package com.zspirytus.enjoymusic.services.media;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.PlayMusicService;

/**
 * Created by ZSpirytus on 2018/8/14.
 */

public class MyMediaSession {

    private static final MyMediaSession INSTANCE = new MyMediaSession();
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;

    private PlayMusicService playMusicService;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat playbackStateCompat;

    private MyMediaSession() {

    }

    public static MyMediaSession getInstance() {
        return INSTANCE;
    }

    public void init(PlayMusicService service) {
        playMusicService = service;
        playbackStateCompat = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                .build();
        mediaSession = new MediaSessionCompat(service, this.getClass().getSimpleName());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);
        mediaSession.setCallback(callback);
        mediaSession.setPlaybackState(playbackStateCompat);
        mediaSession.setActive(true);
    }

    public void setPlaybackState(int state) {
        playbackStateCompat = new PlaybackStateCompat.Builder()
                .setActions(MEDIA_SESSION_ACTIONS)
                .setState(state, MediaPlayController.getInstance().getCurrentPosition(), 1)
                .build();
        mediaSession.setPlaybackState(playbackStateCompat);
    }

    public void setMetaData(Music music) {
        MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getmMusicName())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getmMusicArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeFile(music.getmMusicThumbAlbumUri()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, MusicCache.getInstance().getMusicList().size());
        }
        mediaSession.setMetadata(metaData.build());
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
            MediaPlayController.getInstance().play(MusicCache.getInstance().getCurrentPlayingMusic());
        }

        @Override
        public void onPause() {
            super.onPause();
            MediaPlayController.getInstance().pause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic();
            MediaPlayController.getInstance().play(nextMusic);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            Music previousMusic = MusicPlayOrderManager.getInstance().getPreviousMusic();
            MediaPlayController.getInstance().play(previousMusic);
        }

        @Override
        public void onStop() {
            super.onStop();
            MediaPlayController.getInstance().stop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            MediaPlayController.getInstance().seekTo((int) pos);
        }
    };

}
