package com.zspirytus.enjoymusic.services;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;

import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.entity.Music;

/**
 * Created by ZSpirytus on 2018/8/14.
 */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class MyMediaSession {

    private static final MyMediaSession INSTANCE = new MyMediaSession();
    private static final long MEDIA_SESSION_ACTIONS = PlaybackState.ACTION_PLAY
            | PlaybackState.ACTION_PAUSE
            | PlaybackState.ACTION_PLAY_PAUSE
            | PlaybackState.ACTION_SKIP_TO_NEXT
            | PlaybackState.ACTION_SKIP_TO_PREVIOUS
            | PlaybackState.ACTION_STOP
            | PlaybackState.ACTION_SEEK_TO;

    private PlayMusicService playMusicService;
    private MediaSession mediaSession;

    private MyMediaSession() {

    }

    public static MyMediaSession getInstance() {
        return INSTANCE;
    }

    public void init(PlayMusicService service) {
        playMusicService = service;
        mediaSession = new MediaSession(service, this.getClass().getSimpleName());
        mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS | MediaSession.FLAG_HANDLES_MEDIA_BUTTONS);
        mediaSession.setCallback(callback);
        mediaSession.setActive(true);
    }

    public void setPlaybackState() {
        int state = MediaPlayController.getInstance().isPlaying() ? PlaybackState.STATE_PLAYING : PlaybackState.STATE_PAUSED;
        mediaSession.setPlaybackState(
                new PlaybackState.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(state, MediaPlayController.getInstance().getCurrentPosition(), 1)
                        .build());
    }

    public void setMetaData(Music music) {
        MediaMetadata.Builder metaData = new MediaMetadata.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE, music.getmMusicName())
                .putString(MediaMetadata.METADATA_KEY_ARTIST, music.getmMusicArtist())
                /*.putString(MediaMetadata.METADATA_KEY_ALBUM, music.)*/
                /*.putString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST, music.getArtist())*/
                .putLong(MediaMetadata.METADATA_KEY_DURATION, music.getDuration())
                .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeFile(music.getmMusicThumbAlbumUri()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadata.METADATA_KEY_NUM_TRACKS, MusicCache.getInstance().getMusicList().size());
        }

        mediaSession.setMetadata(metaData.build());
    }

    private MediaSession.Callback callback = new MediaSession.Callback() {
        @Override
        public void onPlay() {
            MediaPlayController.getInstance().play(MusicCache.getInstance().getCurrentPlayingMusic());
        }

        @Override
        public void onPause() {
            MediaPlayController.getInstance().pause();
        }

        @Override
        public void onSkipToNext() {
            MediaPlayController.getInstance().playNext();
        }

        @Override
        public void onSkipToPrevious() {
            MediaPlayController.getInstance().playPrevious();
        }

        @Override
        public void onStop() {
            MediaPlayController.getInstance().stop();
        }

        @Override
        public void onSeekTo(long pos) {
            MediaPlayController.getInstance().seekTo((int) pos);
        }
    };

}
