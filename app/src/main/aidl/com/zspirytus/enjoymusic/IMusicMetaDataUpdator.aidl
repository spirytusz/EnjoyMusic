// IMusicMetaDataUpdator.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.db.table.Artist;

interface IMusicMetaDataUpdator {
    void updateArtist(in Artist artist);
}
