// IGetMusicList.aidl
package com.zspirytus.enjoymusic;

import java.util.List;

import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;

interface IGetMusicList {
    List<Music> getMusicList();
    List<Album> getAlbumList();
    List<Artist> getArtistList();
    List<FolderSortedMusic> getFolderSortedMusic();
}
