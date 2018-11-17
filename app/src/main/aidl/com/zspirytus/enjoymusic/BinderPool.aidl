// BinderPool.aidl
package com.zspirytus.enjoymusic;

interface BinderPool {
    IBinder queryBinder(int binderCode);
    void registerObserver(IBinder observer, int binderCode);
    void unregisterObserver(IBinder observer, int binderCode);
}
