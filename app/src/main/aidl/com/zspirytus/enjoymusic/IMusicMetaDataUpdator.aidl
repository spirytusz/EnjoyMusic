// IMusicMetaDataUpdator.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;

interface IMusicMetaDataUpdator {
    void updateAlbum(in Album album);
    void updateArtist(in Artist artist);
}
