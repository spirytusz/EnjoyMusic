package com.zspirytus.enjoymusic.engine;

import android.support.v4.app.FragmentTransaction;

import com.zspirytus.enjoymusic.view.fragment.BaseFragment;

import java.util.LinkedList;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentVisibilityManager {

    private static final FragmentVisibilityManager ourInstance = new FragmentVisibilityManager();

    private LinkedList<BaseFragment> fragments;
    private int size;

    public static FragmentVisibilityManager getInstance() {
        return ourInstance;
    }

    private FragmentVisibilityManager() {
        fragments = new LinkedList<>();
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

    public void show(FragmentTransaction transaction, BaseFragment shouldShowFragment) {
        for (BaseFragment baseFragment : fragments) {
            if (!baseFragment.equals(shouldShowFragment)) {
                transaction.hide(baseFragment);
            }
        }
        transaction.show(shouldShowFragment);
    }

}
