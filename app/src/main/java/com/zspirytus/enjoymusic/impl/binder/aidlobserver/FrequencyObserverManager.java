package com.zspirytus.enjoymusic.impl.binder.aidlobserver;

import com.zspirytus.enjoymusic.foregroundobserver.IFrequencyObserver;
import com.zspirytus.enjoymusic.receivers.observer.FrequencyChangeObsercer;

import java.util.LinkedList;
import java.util.List;

public class FrequencyObserverManager extends IFrequencyObserver.Stub {

    private List<FrequencyChangeObsercer> observers;

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
        for (FrequencyChangeObsercer observer : observers) {
            observer.onFrequencyChange(magnitudes);
        }
    }

    public void register(FrequencyChangeObsercer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void unregister(FrequencyChangeObsercer observer) {
        observers.remove(observer);
    }
}
