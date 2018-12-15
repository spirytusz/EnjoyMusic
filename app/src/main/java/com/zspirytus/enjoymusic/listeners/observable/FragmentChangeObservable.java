package com.zspirytus.enjoymusic.listeners.observable;

import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.receivers.observer.FragmentChangeObserver;
import com.zspirytus.enjoymusic.view.fragment.BaseFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicCategoryFragment;

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
            if (baseFragmentString.equals(Constant.FragmentName.musicCategoryFragmentName)) {
                int category = ((MusicCategoryFragment) baseFragment).getCurrentPosition();
                if (category == 0) {
                    observerIterator.next().onFragmentChange(Constant.FragmentName.allMusicListFragmentName);
                } else if (category == 1) {
                    observerIterator.next().onFragmentChange(Constant.FragmentName.albumMusicListFragmentName);
                } else if (category == 2) {
                    observerIterator.next().onFragmentChange(Constant.FragmentName.artistMusicListFragmentName);
                } else if (category == 3) {
                    observerIterator.next().onFragmentChange(Constant.FragmentName.folderSortedMusicListFragment);
                }
            } else {
                observerIterator.next().onFragmentChange(baseFragmentString);
            }
        }
    }
}
