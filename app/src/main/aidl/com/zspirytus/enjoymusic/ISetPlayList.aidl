// ISetPlayList.aidl
package com.zspirytus.enjoymusic;

import java.util.List;

import com.zspirytus.enjoymusic.entity.MusicFilter;

interface ISetPlayList {
    void setPlayList(in MusicFilter musicFilter);
}
