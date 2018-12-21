package com.zspirytus.enjoymusic.engine;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.listeners.observable.FragmentChangeObservable;

import java.util.LinkedList;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentVisibilityManager extends FragmentChangeObservable {

    private static final FragmentVisibilityManager ourInstance = new FragmentVisibilityManager();

    private LinkedList<BaseFragment> fragments;
    private FragmentManager mFragmentManager;
    private int size;

    public static FragmentVisibilityManager getInstance() {
        return ourInstance;
    }

    private FragmentVisibilityManager() {
        fragments = new LinkedList<>();
    }

    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void push(BaseFragment fragment) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment);
            size++;
        } else {
            int index = fragments.indexOf(fragment);
            BaseFragment topStackFragment = peek();
            fragments.set(size - 1, fragment);
            fragments.set(index, topStackFragment);
        }
    }

    public BaseFragment peek() {
        if (size > 0) {
            return fragments.get(size - 1);
        } else {
            throw new UnsupportedOperationException("no fragment in fragment stack!");
        }
    }

    public BaseFragment getBackFragment() {
        if (size - 2 >= 0) {
            return fragments.get(size - 2);
        } else {
            throw new UnsupportedOperationException("No back fragment!");
        }
    }

    public void show(BaseFragment shouldShowFragment) {
        show(shouldShowFragment, R.anim.anim_fragment_translate_show_up, R.anim.anim_fragment_translate_show_down);
    }

    public void show(BaseFragment shouldShowFragment, int enter, int exit) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!shouldShowFragment.isAdded()) {
            transaction.add(R.id.fragment_container, shouldShowFragment);
        }
        for (BaseFragment baseFragment : fragments) {
            if (!baseFragment.equals(shouldShowFragment)) {
                transaction.hide(baseFragment);
            } else {
                transaction.show(shouldShowFragment);
            }
        }
        FragmentVisibilityManager.getInstance().push(shouldShowFragment);
        if (enter != 0 && exit != 0) {
            transaction.setCustomAnimations(enter, exit);
        }
        transaction.commitAllowingStateLoss();
        notifyAllFragmentChangeObserver(shouldShowFragment);
    }

}
