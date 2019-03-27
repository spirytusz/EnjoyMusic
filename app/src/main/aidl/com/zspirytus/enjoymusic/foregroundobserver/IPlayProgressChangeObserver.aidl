// IPlayProgressChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

// Declare any non-default types here with import statements

interface IPlayProgressChangeObserver {
    void onProgressChange(long progress);
}
