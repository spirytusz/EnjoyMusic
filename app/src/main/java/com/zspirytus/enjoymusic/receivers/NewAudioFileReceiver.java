package com.zspirytus.enjoymusic.receivers;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.MediaStore;

import com.zspirytus.enjoymusic.cache.MusicScanner;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.INewAudioFileObserver;
import com.zspirytus.enjoymusic.global.MainApplication;

public class NewAudioFileReceiver {

    private static final String TAG = "NewAudioFileReceiver";

    private INewAudioFileObserver mNewAudioFileObserver;

    private static class Singleton {
        static NewAudioFileReceiver INSTANCE = new NewAudioFileReceiver();
    }

    public static NewAudioFileReceiver getInstance() {
        return Singleton.INSTANCE;
    }

    private NewAudioFileReceiver() {
        Handler handler = new Handler(Looper.getMainLooper());
        MainApplication.getAppContext().getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                true,
                new ContentObserver(handler) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                        Music music = MusicScanner.getInstance().scanAddedMusicAndAdd(uri);
                        if (music != null && mNewAudioFileObserver != null) {
                            try {
                                mNewAudioFileObserver.onNewMusic(music);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        MainApplication.getAppContext().getContentResolver().registerContentObserver(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                true,
                new ContentObserver(handler) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                        Album album = MusicScanner.getInstance().scanAddedAlbumAndAdd(uri);
                        if (album != null && mNewAudioFileObserver != null) {
                            try {
                                mNewAudioFileObserver.onNewAlbum(album);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        MainApplication.getAppContext().getContentResolver().registerContentObserver(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                true,
                new ContentObserver(handler) {
                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                        Artist artist = MusicScanner.getInstance().scanAddedArtistAndAdd(uri);
                        if (artist != null && mNewAudioFileObserver != null) {
                            try {
                                mNewAudioFileObserver.onNewArtist(artist);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    public void register(INewAudioFileObserver observer) {
        mNewAudioFileObserver = observer;
    }

    public void unregister() {
        mNewAudioFileObserver = null;
    }
}
