// IPlayProgressChangeObserverManager.aidl
package com.zspirytus.enjoymusic;

import com.zspirytus.enjoymusic.IPlayProgressChangeObserver;

interface IPlayProgressChangeObserverManager {
    void register(IPlayProgressChangeObserver observer);
    void unregister(IPlayProgressChangeObserver observer);
}
