package com.zspirytus.enjoymusic.engine;

import android.support.v4.app.FragmentTransaction;

import com.zspirytus.enjoymusic.view.fragment.BaseFragment;

import java.util.LinkedList;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentStackManager {

    private static final FragmentStackManager ourInstance = new FragmentStackManager();

    private LinkedList<BaseFragment> fragments;
    private int size;

    public static FragmentStackManager getInstance() {
        return ourInstance;
    }

    private FragmentStackManager() {
        fragments = new LinkedList<>();
    }

    public void push(BaseFragment fragment) {
        if (!fragments.contains(fragment)) {
            fragments.add(/*index, */fragment);
            size++;
        } else {
            int index = fragments.indexOf(fragment);
            BaseFragment baseFragment = fragments.peek();
            fragments.set(size - 1, fragment);
            fragments.set(index, baseFragment);
        }
    }

    public BaseFragment pop() {
        int index = --size;
        if (index >= 0)
            return fragments.remove(--size);
        else
            return null;
    }

    public BaseFragment peek() {
        return fragments.peek();
    }

    public void hideAll(FragmentTransaction transaction) {
        for (BaseFragment baseFragment : fragments) {
            transaction.hide(baseFragment);
        }
    }

}
