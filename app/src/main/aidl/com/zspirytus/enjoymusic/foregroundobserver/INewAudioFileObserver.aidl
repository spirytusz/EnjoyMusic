// INewAudioFileObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;

interface INewAudioFileObserver {
    void onNewMusic(in Music music);
    void onNewAlbum(in Album album);
    void onNewArtist(in Artist artist);
}
