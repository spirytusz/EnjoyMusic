package com.zspirytus.enjoymusic.impl;

import android.support.v4.widget.DrawerLayout;
import android.util.SparseArray;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.view.fragment.AboutFragment;
import com.zspirytus.enjoymusic.view.fragment.BaseFragment;
import com.zspirytus.enjoymusic.view.fragment.HomePageFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicCategoryFragment;
import com.zspirytus.enjoymusic.view.fragment.PlayListFragment;
import com.zspirytus.enjoymusic.view.fragment.SettingsFragment;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class DrawerListenerImpl implements DrawerLayout.DrawerListener {

    private SparseArray<Class<? extends BaseFragment>> mNavIdToFragmentClassMapper = new SparseArray<>();
    private int mSelectedNavId;
    private String mCurrentFragmentName;

    public DrawerListenerImpl() {
        mNavIdToFragmentClassMapper.put(R.id.nav_home_page, HomePageFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_music_all, MusicCategoryFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_music_album, MusicCategoryFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_music_artist, MusicCategoryFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_music_folder, MusicCategoryFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_play_list, PlayListFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_settings, SettingsFragment.class);
        mNavIdToFragmentClassMapper.put(R.id.nav_about, AboutFragment.class);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (mSelectedNavId != 0 && slideOffset == 0) {
            Class<? extends BaseFragment> clazz = mNavIdToFragmentClassMapper.get(mSelectedNavId);
            if (mCurrentFragmentName == null || !clazz.getSimpleName().equals(mCurrentFragmentName)) {
                EventBus.getDefault().post(FragmentFactory.getInstance().get(clazz), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                mCurrentFragmentName = clazz.getSimpleName();
            } else if (clazz.getSimpleName().equals(Constant.FragmentName.musicCategoryFragmentName)
                    && mCurrentFragmentName.equals(Constant.FragmentName.musicCategoryFragmentName)) {
                switch (mSelectedNavId) {
                    case R.id.nav_music_all:
                        ((MusicCategoryFragment) FragmentFactory.getInstance().get(clazz)).setCurrentPosition(0);
                        break;
                    case R.id.nav_music_album:
                        ((MusicCategoryFragment) FragmentFactory.getInstance().get(clazz)).setCurrentPosition(1);
                        break;
                    case R.id.nav_music_artist:
                        ((MusicCategoryFragment) FragmentFactory.getInstance().get(clazz)).setCurrentPosition(2);
                        break;
                    case R.id.nav_music_folder:
                        ((MusicCategoryFragment) FragmentFactory.getInstance().get(clazz)).setCurrentPosition(3);
                        break;
                }
            }
            mSelectedNavId = 0;
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    public void setSelectedNavId(int selectedNavId) {
        mSelectedNavId = selectedNavId;
    }
}
