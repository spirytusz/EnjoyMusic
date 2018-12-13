package com.zspirytus.enjoymusic.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.zspirytus.enjoymusic.BinderPool;
import com.zspirytus.enjoymusic.ISetPlayList;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.binder.IPlayMusicChangeObserverImpl;
import com.zspirytus.enjoymusic.adapter.binder.IPlayStateChangeObserverImpl;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.impl.DrawerListenerImpl;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;
import com.zspirytus.enjoymusic.receivers.observer.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.view.fragment.BaseFragment;
import com.zspirytus.enjoymusic.view.fragment.HomePageFragment;
import com.zspirytus.enjoymusic.view.fragment.LaunchAnimationFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicCategoryFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.enjoymusic.view.widget.CustomNavigationView;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Activity: 控制显示、隐藏MusicPlayingFragment、AllMusicListFragment
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.activity_main)
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, PlayedMusicChangeObserver,
        MusicPlayStateObserver, HomePageRecyclerViewLoadObserver, View.OnClickListener {

    @ViewInject(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nav_view)
    private CustomNavigationView mCustomNavigationView;
    @ViewInject(R.id.main_activity_toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.toolbar_shadow)
    private View mToolbarShadow;

    @ViewInject(R.id.bottom_music_control)
    private View mBottomMusicControl;
    @ViewInject(R.id.bottom_music_cover)
    private AppCompatImageView mBottomMusicCover;
    @ViewInject(R.id.bottom_music_name)
    private AppCompatTextView mBottomMusicName;
    @ViewInject(R.id.bottom_music_album)
    private AppCompatTextView mBottomMusicAlbum;
    @ViewInject(R.id.bottom_music_play_pause)
    private AppCompatImageView mBottomMusicPlayOrPause;
    @ViewInject(R.id.bottom_music_next)
    private AppCompatImageView mBottomMusicNext;
    @ViewInject(R.id.bottom_music_control_shadow)
    private View mBottomMusicControlShadow;

    private DrawerListenerImpl mDrawerListener;

    private ServiceConnection conn;
    private ActionBarDrawerToggle toggle;
    private long pressedBackLastTime;
    private boolean isPlaying = false;
    private boolean isHomePageRvLoadFinish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparentStatusBar();

        setFullScreenOrNot(true);
        showLaunchAnimation();

        FragmentFactory.getInstance().get(HomePageFragment.class).setRecyclerViewLoadStateObserver(this);
        bindPlayMusicService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // navigate to selected fragment when mDrawerLayout close completed
        // closing state: begin closing, closing finish closing will listen by DrawerListenerImpl
        mDrawerListener.setSelectedNavId(item.getItemId());
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
        showCastFragment(FragmentFactory.getInstance().get(MusicPlayFragment.class));
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (FragmentVisibilityManager.getInstance().peek() instanceof MusicPlayFragment) {
            changeClickToolbarButtonResponseAndToolbarStyle(true);
            showCastFragment(FragmentVisibilityManager.getInstance().getBackFragment());
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
    protected void initView() {
        setSupportActionBar(mToolbar);
        toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        // TODO: 2018/9/15 has not fixed the bugs:
        // TODO: 2018/9/15 1. When switch MusicCategoryFragment#AlbumMusicListFragment to HomePageFragment, the MusicCategoryFragment has not be hidden.
        // TODO: 2018/9/15 2. viewPager current fragment changed caused by navigation menu selected should be smoothly but not.
        mDrawerListener = new DrawerListenerImpl();
        mDrawerLayout.addDrawerListener(mDrawerListener);
        mBottomMusicControl.setOnClickListener(this);
        mBottomMusicPlayOrPause.setOnClickListener(this);
        mBottomMusicNext.setOnClickListener(this);
        changeClickToolbarButtonResponseAndToolbarStyle(true);
    }

    @Override
    protected void initData() {
        mCustomNavigationView.setNavigationItemSelectedListener(this);
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
        IPlayMusicChangeObserverImpl.getInstance().register(this);
        IPlayStateChangeObserverImpl.getInstance().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
        ForegroundMusicController.getInstance().release();
        mCustomNavigationView.unregisterFragmentChangeListener();
        IPlayMusicChangeObserverImpl.getInstance().unregister(this);
        IPlayStateChangeObserverImpl.getInstance().unregister(this);
    }

    @Override
    public void onGranted() {
        loadMusicList();
    }

    @Override
    public void onPlayedMusicChanged(final Music music) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToolbar.setTitle(music.getMusicName());
                String coverFilePath = music.getMusicThumbAlbumCoverPath();
                if (coverFilePath != null) {
                    File coverFile = new File(coverFilePath);
                    GlideApp.with(MainActivity.this).load(coverFile).into(mBottomMusicCover);
                }
                mBottomMusicName.setText(music.getMusicName());
                mBottomMusicAlbum.setText(music.getMusicAlbumName());
                if (!(FragmentVisibilityManager.getInstance().peek() instanceof MusicPlayFragment)
                        && mBottomMusicControl.getVisibility() == View.GONE) {
                    mBottomMusicControl.setVisibility(View.VISIBLE);
                    mBottomMusicControlShadow.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPlayingStateChanged(final boolean isPlaying) {
        this.isPlaying = isPlaying;
        mBottomMusicPlayOrPause.post(new Runnable() {
            @Override
            public void run() {
                mBottomMusicPlayOrPause.setImageResource(isPlaying ? R.drawable.ic_pause_black_48dp : R.drawable.ic_play_arrow_black_48dp);
            }
        });
    }

    @Override
    public void onHomePageLoadFinish() {
        isHomePageRvLoadFinish = true;
        FragmentFactory.getInstance().get(HomePageFragment.class).setRecyclerViewLoadStateObserver(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_music_control:
                if (ForegroundMusicCache.getInstance().getCurrentPlayingMusic() != null) {
                    showCastFragment(FragmentFactory.getInstance().get(MusicPlayFragment.class));
                }
                break;
            case R.id.bottom_music_play_pause:
                if (isPlaying) {
                    GlideApp.with(MainActivity.this).load(R.drawable.ic_play_arrow_black_48dp).into(mBottomMusicPlayOrPause);
                    ForegroundMusicController.getInstance().pause();
                } else {
                    GlideApp.with(MainActivity.this).load(R.drawable.ic_pause_black_48dp).into(mBottomMusicPlayOrPause);
                    ForegroundMusicController.getInstance().play(ForegroundMusicCache.getInstance().getCurrentPlayingMusic());
                }
                break;
            case R.id.bottom_music_next:
                ForegroundMusicController.getInstance().playNext();
                break;
        }
    }

    @Subscriber(tag = Constant.EventBusTag.SHOW_CAST_FRAGMENT)
    public <T extends BaseFragment> void showCastFragment(T shouldShowFragment) {
        if (shouldShowFragment instanceof MusicCategoryFragment) {
            setToolbarShadowTranslateZ(0.0f);
        } else {
            setToolbarShadowTranslateZ(1.0f);
        }
        if (shouldShowFragment instanceof MusicPlayFragment) {
            changeClickToolbarButtonResponseAndToolbarStyle(false);
            setBottomMusicControlVisibility(View.GONE);
            return;
        } else if (isHomePageRvLoadFinish) {
            setBottomMusicControlVisibility(View.VISIBLE);
        }
        FragmentVisibilityManager.getInstance().show(shouldShowFragment);
    }

    private void loadMusicList() {
        ObservableFactory.getMusicListInForegroundObservable()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        FragmentVisibilityManager.getInstance().init(getSupportFragmentManager());
                        String action = getIntent().getStringExtra(Constant.StatusBarEvent.EXTRA);
                        if (Constant.StatusBarEvent.ACTION_NAME.equals(action)) {
                            showCastFragment(FragmentFactory.getInstance().get(MusicPlayFragment.class));
                        } else {
                            showCastFragment(FragmentFactory.getInstance().get(HomePageFragment.class));
                        }
                        IBinder iBinder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.SET_PLAY_LIST);
                        ISetPlayList setPlayList = ISetPlayList.Stub.asInterface(iBinder);
                        try {
                            setPlayList.setPlayList(MusicFilter.NO_FILTER);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void requestPermission() {
        // request permissions, if it is granted, it will call onGranted()
        ZSPermission.getInstance()
                .at(this)
                .requestCode(123)
                .permissions(PermissionGroup.STORAGE_GROUP)
                .permissions(PermissionGroup.PHONE_GROUP)
                .listenBy(this)
                .request();
    }

    private void setToolbarShadowTranslateZ(float translateZ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbarShadow.setTranslationZ(translateZ);
        }
    }

    private void setBottomMusicControlVisibility(final int visibility) {
        if (mBottomMusicControl.getVisibility() == visibility)
            return;
        Animation animation;
        if (visibility == View.VISIBLE) {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_music_control_show);
        } else {
            animation = AnimationUtils.loadAnimation(this, R.anim.bottom_music_control_hide);
        }
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (visibility == View.VISIBLE) {
                    mBottomMusicControl.setVisibility(View.GONE);
                    mBottomMusicControlShadow.setVisibility(View.GONE);
                } else {
                    mBottomMusicControl.setVisibility(View.VISIBLE);
                    mBottomMusicControlShadow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (visibility == View.VISIBLE) {
                    mBottomMusicControl.setVisibility(View.VISIBLE);
                    mBottomMusicControlShadow.setVisibility(View.VISIBLE);
                } else {
                    mBottomMusicControl.setVisibility(View.GONE);
                    mBottomMusicControlShadow.setVisibility(View.GONE);
                    FragmentVisibilityManager.getInstance().show(FragmentFactory.getInstance().get(MusicPlayFragment.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBottomMusicControl.startAnimation(animation);
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

    private void bindPlayMusicService() {
        Intent startPlayMusicServiceIntent = new Intent(this, PlayMusicService.class);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ForegroundBinderManager.getInstance().init(BinderPool.Stub.asInterface(service));
                requestPermission();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                ForegroundBinderManager.getInstance().release();
            }
        };
        bindService(startPlayMusicServiceIntent, conn, BIND_AUTO_CREATE);
    }

    private void showLaunchAnimation() {
        final LaunchAnimationFragment fragment = new LaunchAnimationFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.launch_animation_container, fragment)
                .show(fragment)
                .commitAllowingStateLoss();
        Observable.timer(3, TimeUnit.SECONDS).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        getSupportFragmentManager().beginTransaction()
                                .hide(fragment)
                                .remove(fragment)
                                .commitAllowingStateLoss();
                        setFullScreenOrNot(false);
                    }
                });
    }

    public static void startActivity(Context context, String extra, String action) {
        Intent intent = new Intent(context, MainActivity.class);
        if (extra != null && action != null) {
            intent.putExtra(extra, action);
        }
        context.startActivity(intent);
    }
}
