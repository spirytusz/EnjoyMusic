// IMusicMetaDataUpdator.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Album;

interface IMusicMetaDataUpdator {
    void updateArtist(in Artist artist);
    void updateAlbum(in Album album);
}
