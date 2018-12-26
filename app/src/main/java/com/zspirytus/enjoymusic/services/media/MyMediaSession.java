package com.zspirytus.enjoymusic.services.media;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.KeyEvent;

import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.utils.LogUtil;

/**
 * MediaSession
 * Created by ZSpirytus on 2018/8/14.
 */

public class MyMediaSession {

    private static final String TAG = "MyMediaSession";
    private static final MyMediaSession INSTANCE = new MyMediaSession();

    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO
            | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
            | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
            | PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM;

    private MediaSessionCompat mediaSession;
    private MediaSessionCompat.Token sessionToken;
    private PlaybackStateCompat playbackStateCompat;

    private MyMediaSession() {
    }

    public static MyMediaSession getInstance() {
        return INSTANCE;
    }

    public void initMediaSession(PlayMusicService service) {
        playbackStateCompat = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                .build();

        ComponentName mbr = new ComponentName(service.getPackageName(), MediaButtonReceiver.class.getName());
        mediaSession = new MediaSessionCompat(service, TAG, mbr, null);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setCallback(callback);
        mediaSession.setPlaybackState(playbackStateCompat);
        mediaSession.setActive(true);
        sessionToken = mediaSession.getSessionToken();
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
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, MusicScanner.getInstance().getAllMusicList().size());
        }
        mediaSession.setMetadata(metaData.build());
    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    public MediaSessionCompat.Token getSessionToken() {
        return sessionToken;
    }

    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            KeyEvent ke = mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (ke != null) {
                LogUtil.e(TAG, "EVENT = " + ke.getAction());
                LogUtil.e(TAG, "CODE = " + ke.getKeyCode());
                LogUtil.e(TAG, "REPEAT" + ke.getRepeatCount());
            }
            return true;
        }
    };

}
