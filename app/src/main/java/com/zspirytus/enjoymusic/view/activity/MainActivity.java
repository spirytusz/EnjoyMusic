package com.zspirytus.enjoymusic.view.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zspirytus.enjoymusic.Interface.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.services.PlayMusicService;
import com.zspirytus.enjoymusic.view.fragment.MusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicPlayFragment;
import com.zspirytus.zspermission.PermissionGroup;
import com.zspirytus.zspermission.ZSPermission;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

public class MainActivity extends BaseActivity implements ZSPermission.OnPermissionListener{

    private static final String TAG = "MainActivity";

    @ViewInject(R.id.mainactivity_toolbar)
    private Toolbar mToolbar;

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private MusicListFragment mMusicListFragment;
    private MusicPlayFragment mMusicPlayFragment;

    private boolean isMusicPlayFragment = false;

    private long pressedBackLastTime;

    private PlayMusicService.MyBinder myBinder;

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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZSPermission.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
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

    private void startPlayMusicService(){
        Intent intent = new Intent(MainActivity.this, PlayMusicService.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    @Subscriber(tag = "play")
    public void play(Music music){
        myBinder.play(music);
    }

    @Subscriber(tag = "pause")
    public void pause(Music music){
        myBinder.pause();
    }

    @Subscriber(tag = "stop")
    public void stop(Music music){
        myBinder.stop();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.BLACK);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void showMusicListFragment() {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        // set animation and show MusicListFragment
        if (mMusicListFragment == null) {
            mMusicListFragment = MusicListFragment.getInstance();
            mFragmentTransaction.add(R.id.fragment_container, mMusicListFragment);
            mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_up,0);
        } else {
            mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_down, R.anim.anim_fragment_alpha_hide);
        }
        if (mMusicPlayFragment != null)
            mFragmentTransaction.hide(mMusicPlayFragment);
        mFragmentTransaction.show(mMusicListFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        isMusicPlayFragment = false;
    }

    @Subscriber(tag="music_play_fragment_show")
    public void showMusicPlayFragment(Music music) {
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.setCustomAnimations(R.anim.anim_fragment_translate_show_up, R.anim.anim_fragment_alpha_hide);
        if (mMusicPlayFragment == null) {
            mMusicPlayFragment = MusicPlayFragment.getInstance(music);
            mFragmentTransaction.add(R.id.fragment_container, mMusicPlayFragment);
        }
        EventBus.getDefault().post(music,"music_name_set");
        if (mMusicListFragment != null)
            mFragmentTransaction.hide(mMusicListFragment);
        mFragmentTransaction.show(mMusicPlayFragment);
        mFragmentTransaction.commitAllowingStateLoss();
        isMusicPlayFragment = true;
    }

}
