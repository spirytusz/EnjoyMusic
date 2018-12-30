package com.zspirytus.enjoymusic.engine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.listeners.observable.FragmentChangeObservable;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentVisibilityManager extends FragmentChangeObservable {

    private static final FragmentVisibilityManager ourInstance = new FragmentVisibilityManager();

    private LinkedList<BaseFragment> fragments;
    private Stack<BaseFragment> backStack;
    private FragmentManager mFragmentManager;

    private BaseFragment mCurrentFragment;

    public static FragmentVisibilityManager getInstance() {
        return ourInstance;
    }

    private FragmentVisibilityManager() {
        fragments = new LinkedList<>();
    }

    public void init(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
        backStack = new Stack<>();
    }

    public BaseFragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void setCurrentFragment(BaseFragment fragment) {
        mCurrentFragment = fragment;
    }

    public void show(BaseFragment shouldShowFragment, int fragmentContainer) {
        show(shouldShowFragment, fragmentContainer, R.anim.anim_fragment_translate_show_up, R.anim.anim_fragment_translate_show_down);
    }

    public void show(BaseFragment shouldShowFragment, int fragmentContainer, int enter, int exit) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!shouldShowFragment.isAdded()) {
            transaction.add(fragmentContainer, shouldShowFragment);
        }
        Fragment fragment = mFragmentManager.findFragmentById(fragmentContainer);
        if (fragment != null) {
            transaction.hide(fragment);
        }
        transaction.show(shouldShowFragment);
        if (enter != 0 && exit != 0) {
            transaction.setCustomAnimations(enter, exit);
        }
        transaction.commitAllowingStateLoss();
        notifyAllFragmentChangeObserver(shouldShowFragment);
        setCurrentFragment(shouldShowFragment);
    }

    public void remove(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
        setCurrentFragment(popBackStack());
    }

    public void addToBackStack(BaseFragment fragment) {
        if (fragment != null && !backStack.contains(fragment)) {
            backStack.push(fragment);
        }
    }

    public BaseFragment popBackStack() {
        if (!backStack.isEmpty())
            return backStack.pop();
        return null;
    }

}
