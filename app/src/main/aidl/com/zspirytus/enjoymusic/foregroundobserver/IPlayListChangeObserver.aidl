// IPlayListChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import com.zspirytus.enjoymusic.entity.MusicFilter;

interface IPlayListChangeObserver {
    void onPlayListChange(in MusicFilter filter);
}
