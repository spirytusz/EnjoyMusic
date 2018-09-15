package com.zspirytus.enjoymusic.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.CurrentPlayingMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentStackManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.impl.OnDraggableFABEventListenerImpl;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.services.media.MediaPlayController;
import com.zspirytus.enjoymusic.utils.AnimationUtil;
import com.zspirytus.enjoymusic.view.fragment.AboutFragment;
import com.zspirytus.enjoymusic.view.fragment.AlbumMusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.AllMusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.ArtistMusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.BaseFragment;
import com.zspirytus.enjoymusic.view.fragment.FragmentFactory;
import com.zspirytus.enjoymusic.view.fragment.HomePageFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicCategoryFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.enjoymusic.view.fragment.SettingsFragment;
import com.zspirytus.mylibrary.DraggableFloatingActionButton;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

/**
 * Activity: 控制显示、隐藏MusicPlayingFragment、AllMusicListFragment
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.activity_main)
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MusicPlayStateObserver {

    private final FragmentManager mFragmentManager = getSupportFragmentManager();

    @ViewInject(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;
    @ViewInject(R.id.main_activity_toolbar)
    public Toolbar mToolbar;
    @ViewInject(R.id.dragged_fab)
    private DraggableFloatingActionButton mFab;

    private ActionBarDrawerToggle toggle;
    private long pressedBackLastTime;


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home_page:
                showCastFragment(FragmentFactory.getInstance().create(HomePageFragment.class));
                break;
            case R.id.nav_music_all:
                showCastFragment(FragmentFactory.getInstance().create(MusicCategoryFragment.class));
                EventBus.getDefault().post(0, Constant.EventBusTag.SET_VIEW_PAGER_CURRENT_ITEM);
                break;
            case R.id.nav_music_album:
                showCastFragment(FragmentFactory.getInstance().create(MusicCategoryFragment.class));
                EventBus.getDefault().post(1, Constant.EventBusTag.SET_VIEW_PAGER_CURRENT_ITEM);
                break;
            case R.id.nav_music_artist:
                showCastFragment(FragmentFactory.getInstance().create(MusicCategoryFragment.class));
                EventBus.getDefault().post(2, Constant.EventBusTag.SET_VIEW_PAGER_CURRENT_ITEM);
                break;
            case R.id.nav_settings:
                showCastFragment(FragmentFactory.getInstance().create(SettingsFragment.class));
                break;
            case R.id.nav_about:
                showCastFragment(FragmentFactory.getInstance().create(AboutFragment.class));
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showMusicPlayFragment(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else if (FragmentStackManager.getInstance().peek() instanceof MusicPlayFragment) {
            changeClickToolbarButtonResponseAndToolbarStyle(true);
            FragmentStackManager.getInstance().pop();
            BaseFragment musicCategoryFragmentOrHomePageFragment = FragmentStackManager.getInstance().peek();
            mFragmentManager.beginTransaction().show(musicCategoryFragmentOrHomePageFragment).commitAllowingStateLoss();
        } else {
            long now = System.currentTimeMillis();
            if (now - pressedBackLastTime < 2 * 1000) {
                finish();
            } else {
                toast("Press back again to quit");
                pressedBackLastTime = now;
            }
        }
    }

    @Override
    public void onPlayingStateChanged(boolean isPlaying) {
        int resId = isPlaying ? R.drawable.ic_pause_white_48dp : R.drawable.ic_play_arrow_white_48dp;
        mFab.setImageResource(resId);
    }

    @Override
    protected void initView() {
        // request permissions, if it is granted, it will call onGranted()
        ZSPermission.getInstance()
                .at(this)
                .requestCode(123)
                .permissions(PermissionGroup.STORAGE_GROUP)
                .permissions(PermissionGroup.PHONE_GROUP)
                .listenBy(this)
                .request();

        setSupportActionBar(mToolbar);
        toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        changeClickToolbarButtonResponseAndToolbarStyle(true);
        onPlayingStateChanged(MediaPlayController.getInstance().isPlaying());
    }

    @Override
    protected void initData() {
        mNavigationView.setNavigationItemSelectedListener(this);
        mFab.setOnDraggableFABEventListener(new OnDraggableFABEventListenerImpl());
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
        MediaPlayController.getInstance().registerMusicPlayStateObserver(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
        MediaPlayController.getInstance().unregisterMusicPlayStateObserver(this);
        ForegroundMusicController.getInstance().release();
    }

    @Override
    public void onGranted() {
        String action = getIntent().getStringExtra(Constant.StatusBarEvent.EXTRA);
        if (Constant.StatusBarEvent.ACTION_NAME.equals(action)) {
            showMusicPlayFragment(CurrentPlayingMusicCache.getInstance().getCurrentPlayingMusic());
        } else {
            showCastFragment(FragmentFactory.getInstance().create(HomePageFragment.class));
        }
    }

    @Subscriber(tag = Constant.EventBusTag.SHOW_MUSIC_PLAY_FRAGMENT)
    public void showMusicPlayFragment(Music music) {
        showCastFragment(FragmentFactory.getInstance().create(MusicPlayFragment.class));
    }

    @Subscriber(tag = Constant.EventBusTag.SET_DFAB_VISIBLE)
    public void setDFabVisible(boolean isDFabVisible) {
        if (isDFabVisible) {
            mFab.setVisibility(View.VISIBLE);
        } else {
            mFab.setVisibility(View.GONE);
        }
    }

    @Subscriber(tag = Constant.EventBusTag.SET_MAIN_ACTIVITY_TOOLBAR_TITLE)
    public void setToolbarTitle(String title) {
        mToolbar.setTitle(title);
    }

    private <T extends BaseFragment> void showCastFragment(T baseFragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        if (!baseFragment.isAdded()) {
            transaction.add(R.id.fragment_container, baseFragment);
        }
        FragmentStackManager.getInstance().hideAll(transaction);
        transaction.show(baseFragment).commitAllowingStateLoss();
        if (baseFragment instanceof MusicCategoryFragment) {
            playWidgetVisibilityAnimation(View.VISIBLE);
        } else {
            playWidgetVisibilityAnimation(View.GONE);
        }
        if (baseFragment instanceof MusicPlayFragment) {
            changeClickToolbarButtonResponseAndToolbarStyle(false);
        } else {
            changeClickToolbarButtonResponseAndToolbarStyle(true);
        }
        if (FragmentStackManager.getInstance().peek() instanceof MusicPlayFragment) {
            FragmentStackManager.getInstance().pop();
        }
        if (!(baseFragment instanceof AllMusicListFragment
                || baseFragment instanceof AlbumMusicListFragment
                || baseFragment instanceof ArtistMusicListFragment)) {
            FragmentStackManager.getInstance().push(baseFragment);
        }
    }

    private void playWidgetVisibilityAnimation(int visibility) {
        if (visibility == View.VISIBLE) {
            AnimationUtil.ofFloat(mFab, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
            mFab.setVisibility(View.VISIBLE);
        } else {
            AnimationUtil.ofFloat(mFab, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
            mFab.setVisibility(View.GONE);
        }
    }

    private void changeClickToolbarButtonResponseAndToolbarStyle(boolean isShouldShowDrawer) {
        if (isShouldShowDrawer) {
            toggle.setDrawerIndicatorEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            mToolbar.setNavigationIcon(R.drawable.ic_rotatable_menu_white);
            mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
        } else {
            toggle.setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationIcon(R.drawable.ic_rotatable_arrow_back_white);
            mDrawerLayout.removeDrawerListener(toggle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.LEFT);
        }
    }

    public static void startActivity(Context context, String extra, String action) {
        Intent intent = new Intent(context, MainActivity.class);
        if (extra != null && action != null) {
            intent.putExtra(extra, action);
        }
        context.startActivity(intent);
    }
}
