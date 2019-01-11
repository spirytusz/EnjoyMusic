// IGetMusicList.aidl
package com.zspirytus.enjoymusic;

import java.util.List;

import com.zspirytus.enjoymusic.entity.Music;

interface IGetMusicList {
    List<Music> getMusicList();
}
