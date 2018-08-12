package com.zspirytus.enjoymusic.view.activity;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.gson.Gson;
import com.zspirytus.enjoymusic.Interface.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.services.MediaPlayHelper;
import com.zspirytus.enjoymusic.services.MusicPlayingObserver;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.view.fragment.MusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;

public class MainActivity extends BaseActivity
        implements ZSPermission.OnPermissionListener, NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, MusicPlayingObserver {

    private static final String TAG = "MainActivity";
    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";

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

    // init view in activity_main.xml
    @ViewInject(R.id.main_drawer)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;

    // init view in include_collapsing_toolbar_layout.xml
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

    private boolean isMusicPlayFragment = false;
    private long pressedBackLastTime;

    private Music currentPlayingMusic;

    @Override
    public Integer getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ZSPermission.getInstance()
                .at(this)
                .requestCode(123)
                .permissions(PermissionGroup.STORAGE_GROUP)
                .permissions(PermissionGroup.PHONE_GROUP)
                .listenBy(this)
                .request();

        initView();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        unbindService(serviceConnection);
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
        switch (id) {
            case R.id.music_previous:
                e("musicPrevious");
                break;
            case R.id.music_next:
                e("musicNext");
                break;
            case R.id.music_play_pause:
                boolean isPlaying = MediaPlayHelper.isPlaying();
                if (isPlaying) {
                    pause();
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

    @Override
    public void onGranted() {
        showMusicListFragment();
        startPlayMusicService();
    }

    @Override
    public void onDenied() {

    }

    @Override
    public void onNeverAsk() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void update(boolean isPlaying) {
        setButtonSrc(MediaPlayHelper.isPlaying());
    }

    @Subscriber(tag = "set current Playing Music")
    private void setCurrentPlayingMusic(Music music) {
        currentPlayingMusic = music;
        play(currentPlayingMusic);
    }

    private void initView() {
        // set status bar style
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.BLACK);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/

        // init toolbar
        mToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_48dp);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // set navigation view menu listener
        mNavigationView.setNavigationItemSelectedListener(this);
        mConstraintLayout.setOnClickListener(this);
        musicPrevious.setOnClickListener(this);
        musicPlayOrPause.setOnClickListener(this);
        musicNext.setOnClickListener(this);
    }

    private void restoreCurrentPlayingMusic() {
        SharedPreferences pref = getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_STRING_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Music music = gson.fromJson(json, Music.class);
            if (music != null) {
                File file = new File(music.getPath());
                if (file.exists()) {
                    currentPlayingMusic = music;

                }
            }
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
        if (mMusicPlayFragment != null)
            mFragmentTransaction.hide(mMusicPlayFragment);
        mFragmentTransaction.show(mMusicListFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        isMusicPlayFragment = false;
    }

    private void startPlayMusicService() {
        Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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

    public void showMusicPlayFragment(Music music) {
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
        isMusicPlayFragment = true;
    }

    public void play(@NonNull Music music) {
        myBinder.play(music);
    }

    public void pause() {
        myBinder.pause();
    }

    public void stop(Music music) {
        myBinder.stop();
    }

}
