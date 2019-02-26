// IPlayHistoryChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

import java.util.List;

import com.zspirytus.enjoymusic.db.table.Music;

interface IPlayHistoryChangeObserver {
    void onPlayHistoryChange(in List<Music> playHistory);
}
