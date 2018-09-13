package com.zspirytus.enjoymusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zspirytus.enjoymusic.cache.constant.Constant;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private int size;

    public MyViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        mFragments = fragments;
        size = fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return size;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return Constant.HomePageTabTitle.ALL;
            case 1:
                return Constant.HomePageTabTitle.ALBUM;
            case 2:
                return Constant.HomePageTabTitle.ARTIST;
        }
        return "";
    }


}
