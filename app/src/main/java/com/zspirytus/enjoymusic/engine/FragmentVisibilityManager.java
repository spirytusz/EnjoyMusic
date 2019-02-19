package com.zspirytus.enjoymusic.engine;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.listeners.observable.FragmentChangeObservable;

import java.util.Stack;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentVisibilityManager extends FragmentChangeObservable {

    private static final String TAG = "FragmentVisibilityManag";

    private static final FragmentVisibilityManager ourInstance = new FragmentVisibilityManager();
    private static final String FRAGMENT_BACK_STACK = "fragmentBackStack";
    private static final String CURRENT_FRAGMENT = "currentFragment";

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

    public void onSaveInstanceState(Bundle outState) {
        // 保存FragmentVisibilityManager必要的数据
        String[] fragmentBackStack = new String[backStack.size()];
        for (int i = 0; i < fragmentBackStack.length; i++) {
            fragmentBackStack[i] = backStack.elementAt(i).getClass().getSimpleName();
        }
        outState.putStringArray(FRAGMENT_BACK_STACK, fragmentBackStack);
        String currentFragment = getCurrentFragment().getClass().getSimpleName();
        outState.putString(CURRENT_FRAGMENT, currentFragment);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // 恢复FragmentVisibilityManager必要的数据
        String[] fragmentBackStack = savedInstanceState.getStringArray(FRAGMENT_BACK_STACK);
        if (fragmentBackStack != null && fragmentBackStack.length > 0) {
            for (String fragmentName : fragmentBackStack) {
                BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(fragmentName);
                addToBackStack(fragment);
            }
        }
        String currentFragment = savedInstanceState.getString(CURRENT_FRAGMENT);
        if (currentFragment != null) {
            BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(currentFragment);
            if (fragment != null) {
                show(fragment);
            }
        }
        FragmentFactory.getInstance().onRestoreState(mFragmentManager);
    }

    public BaseFragment getCurrentFragment() {
        return mCurrentFragment;
    }

    public void show(BaseFragment shouldShowFragment) {
        if (getCurrentFragment() == null || !shouldShowFragment.getClass().getSimpleName().equals(getCurrentFragment().getClass().getSimpleName())) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            int exitAnim = getCurrentFragment() != null ? getCurrentFragment().exitAnim() : 0;
            if (!shouldShowFragment.isAdded()) {
                transaction.add(shouldShowFragment.getContainerId(), shouldShowFragment, shouldShowFragment.getClass().getSimpleName());
            }
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.setCustomAnimations(shouldShowFragment.enterAnim(), exitAnim);
            transaction.show(shouldShowFragment);
            transaction.commitAllowingStateLoss();
            setCurrentFragment(shouldShowFragment);
        }
    }

    public void onChildFragmentChange(BaseFragment parent, BaseFragment child) {
        notifyAllFragmentChangeObserver(child);
    }

    public void remove(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();
        showBackFragment();
    }

    public void hide(BaseFragment fragment) {
        mFragmentManager.beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();
        showBackFragment();
    }

    public void addToBackStack(BaseFragment fragment) {
        if (fragment != null && !backStack.contains(fragment)) {
            backStack.push(fragment);
        }
    }

    public void addCurrentFragmentToBackStack() {
        if (mCurrentFragment != null && !backStack.contains(mCurrentFragment)) {
            backStack.push(mCurrentFragment);
        }
    }

    private void setCurrentFragment(BaseFragment fragment) {
        mCurrentFragment = fragment;
        notifyAllFragmentChangeObserver(fragment);
    }

    private BaseFragment popBackStack() {
        if (!backStack.isEmpty())
            return backStack.pop();
        return null;
    }

    private void showBackFragment() {
        BaseFragment backFragment = popBackStack();
        show(backFragment);
    }

}
