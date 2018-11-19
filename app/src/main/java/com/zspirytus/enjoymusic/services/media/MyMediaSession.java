package com.zspirytus.enjoymusic.services.media;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.utils.LogUtil;

/**
 * MediaSession
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

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat playbackStateCompat;

    private MyMediaSession() {

    }

    public static MyMediaSession getInstance() {
        return INSTANCE;
    }

    public void init(PlayMusicService service) {
        playbackStateCompat = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
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
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, music.getMusicName())
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, music.getMusicArtist())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, music.getMusicDuration())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, BitmapFactory.decodeFile(music.getMusicThumbAlbumCoverPath()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, AllMusicCache.getInstance().getAllMusicList().size());
        }
        mediaSession.setMetadata(metaData.build());
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public void onPlay() {
            super.onPlay();
            //MediaPlayController.getInstance().play(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
            LogUtil.e(this.getClass().getSimpleName(), "MediaSession#onPlay");
        }

        @Override
        public void onPause() {
            super.onPause();
            //MediaPlayController.getInstance().pause();
            LogUtil.e(this.getClass().getSimpleName(), "MediaSession#onPause");
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            /*Music nextMusic = MusicPlayOrderManager.getInstance().getNextMusic();
            MediaPlayController.getInstance().play(nextMusic);*/
            LogUtil.e(this.getClass().getSimpleName(), "MediaSession#onSkipToNext");
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            /*Music previousMusic = MusicPlayOrderManager.getInstance().getPreviousMusic();
            MediaPlayController.getInstance().play(previousMusic);*/
            LogUtil.e(this.getClass().getSimpleName(), "MediaSession#onSkipToPrevious");
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
            System.out.println("MediaSession#onSeekTo");
        }
    };

}
