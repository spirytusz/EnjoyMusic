package com.zspirytus.enjoymusic.listeners.observable;

import android.os.RemoteCallbackList;

import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.foregroundobserver.IPlayListChangeObserver;

import java.util.List;

public abstract class PlayListChangeObservable {

    protected RemoteCallbackList<IPlayListChangeObserver> mObservers = new RemoteCallbackList<>();

    public abstract void register(IPlayListChangeObserver observer);

    public abstract void unregister(IPlayListChangeObserver observer);

    public abstract void notifyAllObserverPlayListChange(List<Music> playList);
}
