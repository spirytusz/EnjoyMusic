package com.zspirytus.enjoymusic.view.activity;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.enjoymusic.AndroidBug5497Workaround;
import com.zspirytus.enjoymusic.IBinderPool;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseActivity;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.global.AudioEffectConfig;
import com.zspirytus.enjoymusic.impl.DrawerListenerImpl;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.view.fragment.HomePageFragment;
import com.zspirytus.enjoymusic.view.fragment.LaunchAnimationFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.enjoymusic.view.widget.CustomNavigationView;
import com.zspirytus.enjoymusic.view.widget.MusicControlPane;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Activity: 控制显示、隐藏Fragment.
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.activity_main)
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @ViewInject(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nav_view)
    private CustomNavigationView mCustomNavigationView;

    @ViewInject(R.id.bottom_music_control)
    private MusicControlPane mBottomMusicControl;

    private DrawerListenerImpl mDrawerListener;

    private MainActivityViewModel mViewModel;
    private ServiceConnection conn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidBug5497Workaround.assistActivity(this);
        bindPlayMusicService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentVisibilityManager.getInstance().onSaveInstanceState(outState);
        AudioEffectConfig.onSaveInstanceState(outState);
        mViewModel.onSaveInstanceState(outState);
    }

    @Override
    protected void onMRestoreInstanceState(Bundle savedInstanceState) {
        setLightStatusIconColor();
        FragmentVisibilityManager.getInstance().init(getSupportFragmentManager());
        FragmentVisibilityManager.getInstance().onRestoreInstanceState(savedInstanceState);
        AudioEffectConfig.onRestoreInstanceState(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            BaseFragment currentFragment = FragmentVisibilityManager.getInstance().getCurrentFragment();
            currentFragment.goBack();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Music action = intent.getParcelableExtra(Constant.NotificationEvent.EXTRA);
        if (action != null) {
            if (!FragmentVisibilityManager.getInstance().getCurrentFragment().getClass().getSimpleName().equals("MusicPlayingFragment")) {
                MusicPlayFragment fragment = FragmentFactory.getInstance().get(MusicPlayFragment.class);
                mViewModel.setCurrentPlayingMusic(action);
                showFragment(fragment);
            }
        }
    }

    @SuppressWarnings("all")
    @Override
    protected void initView() {
        mDrawerListener = new DrawerListenerImpl();
        mDrawerLayout.addDrawerListener(mDrawerListener);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mBottomMusicControl.setOnViewClickListener(new MusicControlPane.OnViewClickListener() {
            @Override
            public void onPlayOrPause() {
                if (mViewModel.getPlayState().getValue()) {
                    ForegroundMusicController.getInstance().pause();
                } else {
                    ForegroundMusicController.getInstance().play(mViewModel.getCurrentPlayingMusic().getValue());
                }
            }

            @Override
            public void onNext() {
                ForegroundMusicController.getInstance().playNext(true);
            }

            @Override
            public void onClick() {
                if (mViewModel.getCurrentPlayingMusic().getValue() != null) {
                    showFragment(FragmentFactory.getInstance().get(MusicPlayFragment.class));
                }
            }
        });
        mCustomNavigationView.setNavigationItemSelectedListener(this);
        fixNavBarHeight(mBottomMusicControl);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mViewModel.init();
    }

    @Override
    protected void initData() {
        FragmentVisibilityManager.getInstance().init(getSupportFragmentManager());
    }

    @Override
    protected void unregisterEvent() {
        ForegroundMusicController.getInstance().release();
        mCustomNavigationView.unregisterFragmentChangeListener();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // navigate to selected fragment when mDrawerLayout close completed
        // closing state: begin closing, closing finish closing will listen by DrawerListenerImpl
        mDrawerListener.setSelectedNavId(item.getItemId());
        mDrawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    public void onGranted() {
        ForegroundBinderManager.getInstance().registerEvent();
        mViewModel.obtainMusicList();
        mViewModel.getCurrentPlayingMusic().observe(this, values -> {
            mBottomMusicControl.wrapMusic(values);
        });
        mViewModel.getPlayState().observe(this, values -> {
            mBottomMusicControl.setPlayState(values);
        });
    }

    private void showFragment(BaseFragment fragment) {
        if (fragment instanceof MusicPlayFragment) {
            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
        }
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    private void requestPermission() {
        // request permissions, if it is granted, it will call onGranted()
        ZSPermission.getInstance()
                .at(this)
                .requestCode(123)
                .permissions(PermissionGroup.STORAGE_GROUP)
                .permission(Manifest.permission.RECORD_AUDIO)
                .listenBy(this)
                .request();

    }

    private void bindPlayMusicService() {
        Intent startPlayMusicServiceIntent = new Intent(this, PlayMusicService.class);
        /*
         * 先启动Service，走onStartCommand并返回START_STICKY.
         * 这样Service就一直是粘性的.
         */
        startService(startPlayMusicServiceIntent);
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ForegroundBinderManager.getInstance().init(IBinderPool.Stub.asInterface(service));
                /*
                 * MusicPlayFragment中LiveData变化的回调事件中带有binder的使用
                 * 所以显示MusicPlayFragment必须在Binder初始化后才能执行
                 */
                Music music = getIntent().getParcelableExtra(Constant.NotificationEvent.EXTRA);
                if (music != null) {
                    mViewModel.setCurrentPlayingMusic(music);
                    MusicPlayFragment fragment = FragmentFactory.getInstance().get(MusicPlayFragment.class);
                    showFragment(fragment);
                    BaseFragment homeFragment = FragmentFactory.getInstance().get(HomePageFragment.class);
                    FragmentVisibilityManager.getInstance().addToBackStack(homeFragment);
                } else {
                    showFragment(FragmentFactory.getInstance().get(HomePageFragment.class));
                }
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
        setFullScreenOrNot(true);
        final LaunchAnimationFragment fragment = new LaunchAnimationFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.launch_animation_container, fragment)
                .show(fragment)
                .commitAllowingStateLoss();
        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commitAllowingStateLoss();
            setFullScreenOrNot(false);
        }, 3, TimeUnit.SECONDS);
    }
}
