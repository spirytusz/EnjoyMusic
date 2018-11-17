// IPlayStateChangeObserverManager.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.IPlayStateChangeObserver;

interface IPlayStateChangeObserverManager {
    void register(IPlayStateChangeObserver observer);
    void unregister(IPlayStateChangeObserver observer);
}
