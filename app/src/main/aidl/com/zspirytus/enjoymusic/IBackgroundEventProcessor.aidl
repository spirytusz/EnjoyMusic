// IBackgroundEventProcessor.aidl
package com.zspirytus.enjoymusic;

interface IBackgroundEventProcessor {
    void registerObserver(IBinder observer, int binderCode);
    void unregisterObserver(IBinder observer, int binderCode);
}
