package com.zspirytus.enjoymusic.impl;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.view.fragment.AboutFragment;
import com.zspirytus.enjoymusic.view.fragment.HomePageFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicCategoryFragment;
import com.zspirytus.enjoymusic.view.fragment.PlayListFragment;
import com.zspirytus.enjoymusic.view.fragment.SettingsFragment;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class DrawerListenerImpl implements DrawerLayout.DrawerListener {

    private int mSelectedNavId;
    private int mLastSelectNavId = -1;

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        FragmentFactory factory = FragmentFactory.getInstance();
        if (mSelectedNavId != mLastSelectNavId) {
            switch (mSelectedNavId) {
                case R.id.nav_home_page:
                    EventBus.getDefault().post(factory.get(HomePageFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                    break;
                case R.id.nav_music_all:
                    factory.get(MusicCategoryFragment.class).setCurrentPosition(0);
                    showMusicCategoryFragmentOrNot();
                    break;
                case R.id.nav_music_album:
                    factory.get(MusicCategoryFragment.class).setCurrentPosition(1);
                    showMusicCategoryFragmentOrNot();
                    break;
                case R.id.nav_music_artist:
                    factory.get(MusicCategoryFragment.class).setCurrentPosition(2);
                    showMusicCategoryFragmentOrNot();
                    break;
                case R.id.nav_music_folder:
                    factory.get(MusicCategoryFragment.class).setCurrentPosition(3);
                    showMusicCategoryFragmentOrNot();
                    break;
                case R.id.nav_play_list:
                    EventBus.getDefault().post(factory.get(PlayListFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                    break;
                case R.id.nav_settings:
                    EventBus.getDefault().post(factory.get(SettingsFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                    break;
                case R.id.nav_about:
                    EventBus.getDefault().post(factory.get(AboutFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                    break;
            }
        }
        mLastSelectNavId = mSelectedNavId;
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    public void setSelectedNavId(int selectedNavId) {
        mSelectedNavId = selectedNavId;
    }

    private void showMusicCategoryFragmentOrNot() {
        if (!isMusicCategoryFragment()) {
            EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicCategoryFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
        }
    }

    private boolean isMusicCategoryFragment() {
        return mLastSelectNavId == R.id.nav_music_all || mLastSelectNavId == R.id.nav_music_album
                || mLastSelectNavId == R.id.nav_music_artist || mLastSelectNavId == R.id.nav_music_folder;
    }
}
