package com.zspirytus.enjoymusic.listeners.observable;

import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.receivers.observer.FragmentChangeObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/10/16.
 */

public class FragmentChangeObservable {

    private List<FragmentChangeObserver> mFragmentChangeObservers;

    protected FragmentChangeObservable() {
        mFragmentChangeObservers = new ArrayList<>();
    }

    public void register(FragmentChangeObserver observer) {
        if (!mFragmentChangeObservers.contains(observer)) {
            mFragmentChangeObservers.add(observer);
        }
    }

    public void unregister(FragmentChangeObserver observer) {
        mFragmentChangeObservers.remove(observer);
    }

    protected void notifyAllFragmentChangeObserver(BaseFragment baseFragment) {
        String baseFragmentString = baseFragment.getClass().getSimpleName();
        Iterator<FragmentChangeObserver> observerIterator = mFragmentChangeObservers.iterator();
        while (observerIterator.hasNext()) {
            observerIterator.next().onFragmentChange(baseFragmentString);
        }
    }
}
