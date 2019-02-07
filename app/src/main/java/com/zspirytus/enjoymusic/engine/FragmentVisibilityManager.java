package com.zspirytus.enjoymusic.engine;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.listeners.observable.FragmentChangeObservable;

import java.util.Stack;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentVisibilityManager extends FragmentChangeObservable {

    private static final FragmentVisibilityManager ourInstance = new FragmentVisibilityManager();

    private Stack<BaseFragment> backStack;
    private FragmentManager mFragmentManager;

    private BaseFragment mCurrentFragment;

    public static FragmentVisibilityManager getInstance() {
        return ourInstance;
    }

    private FragmentVisibilityManager() {
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
        notifyAllFragmentChangeObserver(fragment);
    }

    public void show(BaseFragment shouldShowFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int exitAnim = getCurrentFragment() != null ? getCurrentFragment().exitAnim() : 0;
        if (!shouldShowFragment.isAdded()) {
            transaction.add(shouldShowFragment.getContainerId(), shouldShowFragment);
        }
        if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
        }
        transaction.setCustomAnimations(shouldShowFragment.enterAnim(), exitAnim);
        transaction.show(shouldShowFragment);
        transaction.commitAllowingStateLoss();
        setCurrentFragment(shouldShowFragment);
    }

    public void remove(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
        showBackFragment(fragment);
    }

    public void hide(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();
        showBackFragment(fragment);
    }

    public void addToBackStack(BaseFragment fragment) {
        if (fragment != null && !backStack.contains(fragment)) {
            backStack.push(fragment);
        }
    }

    private BaseFragment popBackStack() {
        if (!backStack.isEmpty())
            return backStack.pop();
        return null;
    }

    private void showBackFragment(BaseFragment invisibleFragment) {
        BaseFragment backFragment = popBackStack();
        show(backFragment);
    }

}
