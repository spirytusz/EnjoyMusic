// IPlayStateChangeObserver.aidl
package com.zspirytus.enjoymusic.foregroundobserver;

interface IPlayStateChangeObserver {
    void onPlayStateChange(boolean isPlaying);
}
