package com.zspirytus.enjoymusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.view.widget.lazyviewpager.LazyFragmentPagerAdapter;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

public class MyViewPagerAdapter extends LazyFragmentPagerAdapter {

    private List<BaseFragment> mFragments;
    private int size;

    public MyViewPagerAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        mFragments = fragments;
        size = fragments.size();
    }

    @Override
    protected Fragment getItem(ViewGroup container, int position) {
        return mFragments.get(position);
    }

    public BaseFragment getItem(int position) {
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
                return MainApplication.getAppContext().getResources().getText(R.string.all_music);
            case 1:
                return MainApplication.getAppContext().getResources().getText(R.string.album);
            case 2:
                return MainApplication.getAppContext().getResources().getText(R.string.artist);
            case 3:
                return MainApplication.getAppContext().getResources().getText(R.string.folder);
            case 4:
                return "歌单";
        }
        return "";
    }


}
