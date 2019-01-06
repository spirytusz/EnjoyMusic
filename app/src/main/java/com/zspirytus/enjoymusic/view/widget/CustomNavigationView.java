package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.AttributeSet;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.receivers.observer.FragmentChangeObserver;

/**
 * Created by ZSpirytus on 2018/10/16.
 */

public class CustomNavigationView extends NavigationView implements FragmentChangeObserver {
    public CustomNavigationView(Context context) {
        this(context, null);
    }

    public CustomNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        registerFragmentChangeListener();
        setCheckedItem(R.id.nav_home_page);
    }

    private void registerFragmentChangeListener() {
        FragmentVisibilityManager.getInstance().register(this);
    }

    public void unregisterFragmentChangeListener() {
        FragmentVisibilityManager.getInstance().unregister(this);
    }

    @Override
    public void onFragmentChange(String newFragmentName) {
        switch (newFragmentName) {
            case Constant.FragmentName.homePageFragmentName:
                setCheckedItem(R.id.nav_home_page);
                break;
            case Constant.FragmentName.allMusicListFragmentName:
                setCheckedItem(R.id.nav_music_all);
                break;
            case Constant.FragmentName.albumMusicListFragmentName:
                setCheckedItem(R.id.nav_music_album);
                break;
            case Constant.FragmentName.artistMusicListFragmentName:
                setCheckedItem(R.id.nav_music_artist);
                break;
            case Constant.FragmentName.folderSortedMusicListFragment:
                setCheckedItem(R.id.nav_music_folder);
            case Constant.FragmentName.playListFragmentName:
                setCheckedItem(R.id.nav_play_list);
                break;
            case Constant.FragmentName.settingFragmentName:
                setCheckedItem(R.id.nav_settings);
                break;
            case Constant.FragmentName.aboutFragmentName:
                setCheckedItem(R.id.nav_about);
                break;
        }
    }
}
