package com.zspirytus.enjoymusic.receivers.observer;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;

public interface NewAudioFileObserver {
    void onNewMusic(Music music);

    void onNewAlbum(Album album);

    void onNewArtist(Artist artist);
}
