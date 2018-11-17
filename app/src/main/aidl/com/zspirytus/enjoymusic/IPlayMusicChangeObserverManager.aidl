// IPlayMusicChangeObserverManager.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.IPlayMusicChangeObserver;

interface IPlayMusicChangeObserverManager {
    void register(IPlayMusicChangeObserver observer);
    void unregister(IPlayMusicChangeObserver observer);
}
