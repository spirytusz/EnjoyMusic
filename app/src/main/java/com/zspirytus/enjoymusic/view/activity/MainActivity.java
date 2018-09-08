package com.zspirytus.enjoymusic.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.impl.OnDraggableFABEventListenerImpl;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.view.fragment.MusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.mylibrary.DraggableFloatingActionButton;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Activity: 控制显示、隐藏MusicPlayingFragment、MusicListFragment
 * Created by ZSpirytus on 2018/8/2.
 */

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MusicPlayStateObserver {

    // init view in activity_main.xml
    @ViewInject(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;

    // init view in include_main_container.xml
    @ViewInject(R.id.main_activity_toolbar)
    private Toolbar mToolbar;

    // init draggable fab
    @ViewInject(R.id.dragged_fab)
    private DraggableFloatingActionButton mFab;

    // Fragments and Fragment Manager
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private MusicListFragment mMusicListFragment;
    private MusicPlayFragment mMusicPlayFragment;

    private boolean isMusicPlayFragment = false;
    private long pressedBackLastTime;

    @Override
    public Integer getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEvent();
        initData();

        initView();
    }

    @Override
    public void onDestroy() {
        unregisterEvent();
        MusicCache musicCache = MusicCache.getInstance();
        musicCache.saveCurrentPlayingMusic();
        ForegroundMusicController.getInstance().release();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (isMusicPlayFragment) {
            // hide MusicPlayFragment and show MusicListFragment
            showMusicListFragment();
            return;
        } else {
            // exit or not
            long now = System.currentTimeMillis();
            if (now - pressedBackLastTime < 2 * 1000) {
                finish();
            } else {
                toast("Press back again to quit");
                pressedBackLastTime = now;
            }
        }
    }

    // permission granted state listener
    @Override
    public void onGranted() {
        showMusicListFragment();
        startPlayMusicService();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPlayingState(boolean isPlaying) {
        // set the play or pause button src
        int resId = isPlaying ? R.drawable.ic_pause_white_48dp : R.drawable.ic_play_arrow_white_48dp;
        mFab.setImageResource(resId);
    }

    @Override
    public void onPlayCompleted() {
        // set the play or pause button src be pause(play src) state
    }

    private void initView() {
        // request permissions or show music list fragment
        ZSPermission.getInstance()
                .at(this)
                .requestCode(123)
                .permissions(PermissionGroup.STORAGE_GROUP)
                .permissions(PermissionGroup.PHONE_GROUP)
                .listenBy(this)
                .request();

        // init toolbar
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        onPlayingState(MediaPlayController.getInstance().isPlaying());

        // set listener
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void initData() {

    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
        startService(intent);
    }

    private void showMusicListFragment() {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        // set animation and show MusicListFragment
        if (mMusicListFragment == null) {
            mMusicListFragment = MusicListFragment.getInstance();
            mFragmentTransaction.add(R.id.fragment_container, mMusicListFragment);
            mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_up, 0);
        } else {
            mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_down, R.anim.anim_fragment_alpha_hide);
        }
        if (mMusicPlayFragment != null) {
            mFragmentTransaction.hide(mMusicPlayFragment);
        }
        mFragmentTransaction.show(mMusicListFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        mToolbar.setVisibility(View.VISIBLE);
        mFab.setVisibility(View.VISIBLE);
        isMusicPlayFragment = false;
    }

    @Subscriber(tag = FinalValue.EventBusTag.SHOW_MUSIC_PLAY_FRAGMENT)
    public void showMusicPlayFragment(Music music) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_up, R.anim.anim_fragment_alpha_hide);
        if (mMusicPlayFragment == null) {
            mMusicPlayFragment = MusicPlayFragment.getInstance();
            mFragmentTransaction.add(R.id.fragment_container, mMusicPlayFragment);
        }
        if (mMusicListFragment != null)
            mFragmentTransaction.hide(mMusicListFragment);
        mFragmentTransaction.show(mMusicPlayFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        mToolbar.setVisibility(View.GONE);
        mFab.setVisibility(View.GONE);
        isMusicPlayFragment = true;
    }

    // if there is no music in device, it will not set DraggableFabListener.
    @Subscriber(tag = FinalValue.EventBusTag.SET_DFAB_LISTENER)
    public void setDraggableFabListener(boolean hasMusicInDevice) {
        if (hasMusicInDevice) {
            mFab.setOnDraggableFABEventListener(new OnDraggableFABEventListenerImpl(mFab));
        }
    }

    private void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().register(this);
    }

    private void unregisterEvent() {
        EventBus.getDefault().unregister(this);
        MediaPlayController.getInstance().unregister(this);
    }

}
