// IMusicMetaDataUpdator.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Music;

interface IMusicMetaDataUpdator {
    void updateArtist(in Artist artist);
    boolean updateAlbum(in Album album);
    boolean deleteMusic(in Music music);
}
