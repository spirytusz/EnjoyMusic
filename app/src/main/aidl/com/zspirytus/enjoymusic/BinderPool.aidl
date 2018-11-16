// BinderPoolImpl.aidl
package com.zspirytus.enjoymusic;

interface BinderPool {
    IBinder queryBinder(int binderCode);
}
