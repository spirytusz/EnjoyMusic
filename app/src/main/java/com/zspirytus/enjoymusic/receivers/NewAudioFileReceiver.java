package com.zspirytus.enjoymusic.receivers;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.MediaStore;

import com.zspirytus.basesdk.utils.LogUtil;
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
        ContentResolver resolver = MainApplication.getAppContext().getContentResolver();
        ContentObserver musicAddedObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                /*
                 * 这里不知道为什么会有IllegalStateException异常，貌似是系统的Bug。。。
                 * 捕捉它不做处理吧。。
                 */
                try {
                    Music music = MusicScanner.getInstance().scanAddedMusicAndAdd(uri);
                    if (music != null && mNewAudioFileObserver != null) {
                        mNewAudioFileObserver.onNewMusic(music);
                    }
                } catch (IllegalStateException e) {
                    LogUtil.log(MainApplication.getAppContext(), "caught_ex.log", e);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        ContentObserver albumAddedObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                /*
                 * 这里不知道为什么会有IllegalStateException异常，貌似是系统的Bug。。。
                 * 捕捉它不做处理吧。。
                 */
                try {
                    Album album = MusicScanner.getInstance().scanAddedAlbumAndAdd(uri);
                    if (album != null && mNewAudioFileObserver != null) {
                        mNewAudioFileObserver.onNewAlbum(album);
                    }
                } catch (IllegalStateException e) {
                    LogUtil.log(MainApplication.getAppContext(), "caught_ex.log", e);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        ContentObserver artistAddedObserver = new ContentObserver(handler) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                /*
                 * 这里不知道为什么会有IllegalStateException异常，貌似是系统的Bug。。。
                 * 捕捉它不做处理吧。。
                 */
                try {
                    Artist artist = MusicScanner.getInstance().scanAddedArtistAndAdd(uri);
                    if (artist != null && mNewAudioFileObserver != null) {
                        mNewAudioFileObserver.onNewArtist(artist);
                    }
                } catch (IllegalStateException e) {
                    LogUtil.log(MainApplication.getAppContext(), "caught_ex.log", e);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        resolver.registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                true,
                musicAddedObserver
        );
        resolver.registerContentObserver(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                true,
                albumAddedObserver
        );
        resolver.registerContentObserver(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                true,
                artistAddedObserver
        );
    }

    public void register(INewAudioFileObserver observer) {
        mNewAudioFileObserver = observer;
    }

    public void unregister() {
        mNewAudioFileObserver = null;
    }
}
