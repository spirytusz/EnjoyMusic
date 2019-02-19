package com.zspirytus.enjoymusic.impl;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.view.fragment.AboutFragment;
import com.zspirytus.enjoymusic.view.fragment.HomePageFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicCategoryFragment;
import com.zspirytus.enjoymusic.view.fragment.PlayListFragment;
import com.zspirytus.enjoymusic.view.fragment.SettingsFragment;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class DrawerListenerImpl implements DrawerLayout.DrawerListener {

    private static final String TAG = "DrawerListenerImpl";

    private int mSelectedNavId;

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
    }

    @Override
    public void onDrawerOpened(View drawerView) {
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        FragmentFactory factory = FragmentFactory.getInstance();
        switch (mSelectedNavId) {
            case R.id.nav_home_page:
                FragmentVisibilityManager.getInstance().show(factory.get(HomePageFragment.class));
                break;
            case R.id.nav_music_all:
                showMusicCategoryFragmentOrNot(0);
                break;
            case R.id.nav_music_album:
                showMusicCategoryFragmentOrNot(1);
                break;
            case R.id.nav_music_artist:
                showMusicCategoryFragmentOrNot(2);
                break;
            case R.id.nav_music_folder:
                showMusicCategoryFragmentOrNot(3);
                break;
            case R.id.nav_song_list:
                showMusicCategoryFragmentOrNot(4);
                break;
            case R.id.nav_play_list:
                FragmentVisibilityManager.getInstance().show(factory.get(PlayListFragment.class));
                break;
            case R.id.nav_settings:
                FragmentVisibilityManager.getInstance().show(factory.get(SettingsFragment.class));
                break;
            case R.id.nav_about:
                FragmentVisibilityManager.getInstance().show(factory.get(AboutFragment.class));
                break;
        }
    }

    @Override
    public void onDrawerStateChanged(int newState) {
    }

    public void setSelectedNavId(int selectedNavId) {
        mSelectedNavId = selectedNavId;
    }

    private void showMusicCategoryFragmentOrNot(int category) {
        MusicCategoryFragment fragment = FragmentFactory.getInstance().get(MusicCategoryFragment.class);
        if (fragment.getArguments() == null) {
            Bundle bundle = new Bundle();
            bundle.putInt("currentPosition", category);
            fragment.setArguments(bundle);
        } else {
            fragment.setCurrentPosition(category);
        }
        FragmentVisibilityManager.getInstance().show(fragment);
    }
}
