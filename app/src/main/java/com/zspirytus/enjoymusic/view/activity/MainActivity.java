package com.zspirytus.enjoymusic.view.activity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.receivers.MusicPlayStateObserver;
import com.zspirytus.enjoymusic.services.MediaPlayController;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.view.fragment.MusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

/**
 * Activity: 控制显示、隐藏MusicPlayingFragment、MusicListFragment
 * Created by ZSpirytus on 2018/8/2.
 */

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, MusicPlayStateObserver {

    // init view in activity_main.xml
    @ViewInject(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;

    // init view in include_main_container.xml
    @ViewInject(R.id.main_activity_toolbar)
    private Toolbar mToolbar;

    // init view in bottom_music_bar.xml
    @ViewInject(R.id.music_bottom)
    private ConstraintLayout mConstraintLayout;
    @ViewInject(R.id.music_previous)
    private ImageView musicPrevious;
    @ViewInject(R.id.music_play_pause)
    private ImageView musicPlayOrPause;
    @ViewInject(R.id.music_next)
    private ImageView musicNext;
    @ViewInject(R.id.music_thumb_cover)
    private ImageView mCover;
    @ViewInject(R.id.music_name)
    private TextView mMusicName;
    @ViewInject(R.id.music_artist)
    private TextView mMusicArtist;
    @ViewInject(R.id.music_progressBar)
    private ProgressBar mMusicProgress;

    // Fragments and Fragment Manager
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private MusicListFragment mMusicListFragment;
    private MusicPlayFragment mMusicPlayFragment;

    // Service's Binder
    private PlayMusicService.MyBinder myBinder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (PlayMusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBinder = null;
        }
    };

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

        initView();
    }

    @Override
    public void onDestroy() {
        unregisterEvent();
        unbindService(serviceConnection);
        MusicCache musicCache = MusicCache.getInstance();
        musicCache.saveCurrentPlayingMusic(musicCache.getCurrentPlayingMusic());
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Music currentPlayingMusic = MusicCache.getInstance().getCurrentPlayingMusic();
        switch (id) {
            case R.id.music_previous:
                play(MusicCache.getInstance().getPreviousMusic(MusicCache.MODE_ORDER));
                break;
            case R.id.music_next:
                play(MusicCache.getInstance().getNextMusic(MusicCache.MODE_ORDER));
                break;
            case R.id.music_play_pause:
                boolean isPlaying = MediaPlayController.getInstance().isPlaying();
                if (isPlaying) {
                    pause(currentPlayingMusic);
                } else {
                    play(currentPlayingMusic);
                }
                break;
            case R.id.music_bottom:
                if (currentPlayingMusic != null) {
                    showMusicPlayFragment(currentPlayingMusic);
                }
                break;
            default:
                break;
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
        setButtonSrc(isPlaying);
    }

    @Override
    public void onPlayCompleted() {
        // set the play or pause button src be pause(play src) state
        setButtonSrc(false);
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

        // set navigation view menu listener
        mNavigationView.setNavigationItemSelectedListener(this);
        mConstraintLayout.setOnClickListener(this);
        musicPrevious.setOnClickListener(this);
        musicPlayOrPause.setOnClickListener(this);
        musicNext.setOnClickListener(this);

        // restore currentPlayingMusic
        setMusicBarView(MusicCache.getInstance().getCurrentPlayingMusic());
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void setMusicBarView(Music music) {
        if (music != null) {
            String albumDir = music.getmMusicThumbAlbumUri();
            if (albumDir != null) {
                Glide.with(this).load(new File(albumDir)).into(mCover);
            }
            mMusicName.setText(music.getmMusicName());
            mMusicArtist.setText(music.getmMusicArtist());
        }
    }

    private void setButtonSrc(boolean isPlaying) {
        if (isPlaying) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 1f, 0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_pause_thin).into(musicPlayOrPause);
            animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 0f, 1f);
            animator.setDuration(382);
            animator.start();
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 1f, 0f);
            animator.setDuration(382);
            animator.start();
            Glide.with(this).load(R.drawable.ic_play_thin).into(musicPlayOrPause);
            animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 0f, 1f);
            animator.setDuration(382);
            animator.start();
        }
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
            setmConstraintLayoutAndToolbarVisible(true);
        }
        mFragmentTransaction.show(mMusicListFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        mToolbar.setVisibility(View.VISIBLE);
        isMusicPlayFragment = false;
    }

    private void showMusicPlayFragment(Music music) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_up, R.anim.anim_fragment_alpha_hide);
        if (mMusicPlayFragment == null) {
            mMusicPlayFragment = MusicPlayFragment.getInstance(music);
            mFragmentTransaction.add(R.id.fragment_container, mMusicPlayFragment);
        }
        EventBus.getDefault().post(music, "music_name_set");
        if (mMusicListFragment != null)
            mFragmentTransaction.hide(mMusicListFragment);
        mFragmentTransaction.show(mMusicPlayFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        setmConstraintLayoutAndToolbarVisible(false);
        mToolbar.setVisibility(View.GONE);
        isMusicPlayFragment = true;
    }

    private void setmConstraintLayoutAndToolbarVisible(boolean visible) {
        if (visible) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mConstraintLayout, "alpha", 0f, 1f);
            animator.setDuration(618);
            animator.start();
            animator = ObjectAnimator.ofFloat(mToolbar, "alpha", 0f, 1f);
            animator.setDuration(618);
            animator.start();
            mConstraintLayout.setVisibility(View.VISIBLE);
            mToolbar.setVisibility(View.VISIBLE);
        } else {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mConstraintLayout, "alpha", 1f, 0f);
            animator.setDuration(618);
            animator.start();
            animator = ObjectAnimator.ofFloat(mToolbar, "alpha", 1f, 0f);
            animator.setDuration(618);
            animator.start();
            mConstraintLayout.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
        }
    }

    @Subscriber(tag = "play")
    public void play(Music music) {
        if (music != null) {
            myBinder.play(music);
            setMusicBarView(music);
        }
    }

    @Subscriber(tag = "pause")
    public void pause(Music music) {
        myBinder.pause();
    }

    @Subscriber(tag = "stop")
    public void stop(Music music) {
        myBinder.stop();
    }

    @Subscriber(tag = "seek to")
    public void seekTo(int progress) {
        myBinder.seekTo(progress);
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
