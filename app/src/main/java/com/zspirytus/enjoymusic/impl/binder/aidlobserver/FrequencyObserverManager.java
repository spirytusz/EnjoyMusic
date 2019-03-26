package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import com.zspirytus.enjoymusic.foregroundobserver.IFrequencyObserver;
import com.zspirytus.enjoymusic.receivers.observer.OnFrequencyChangeListener;

import java.util.LinkedList;
import java.util.List;

public class FrequencyObserverManager extends IFrequencyObserver.Stub {

    private List<OnFrequencyChangeListener> observers;

    private static class Singleton {
        static FrequencyObserverManager INSTANCE = new FrequencyObserverManager();
    }

    private FrequencyObserverManager() {
        observers = new LinkedList<>();
    }

    public static FrequencyObserverManager getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void onFrequencyChange(float[] magnitudes) {
        for (OnFrequencyChangeListener observer : observers) {
            observer.onFrequencyChange(magnitudes);
        }
    }

    public void register(OnFrequencyChangeListener observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(OnFrequencyChangeListener observer) {
        observers.remove(observer);
    }
}
