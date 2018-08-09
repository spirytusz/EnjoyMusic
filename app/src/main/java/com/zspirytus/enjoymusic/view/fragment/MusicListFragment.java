package com.zspirytus.enjoymusic.view.fragment;

import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zspirytus.enjoymusic.Interface.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.model.Music;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ZSpirytus on 2018/8/2.
 */

public class MusicListFragment extends BaseFragment implements View.OnClickListener, MusicListAdapter.OnItemClickListener {

    private static final String CURRENT_PLAYING_MUSIC = "currentPlayingMusic";
    private static final String CURRENT_PLAYING_MUSIC_STRING_KEY = "currentPlayingMusicString";

    @ViewInject(R.id.backgroud_img)
    private ImageView mBackgroundImage;
    @ViewInject(R.id.collapsing_layout)
    private CollapsingToolbarLayout mCtl;
    @ViewInject(R.id.mainactivity_fab)
    private FloatingActionButton mFab;
    @ViewInject(R.id.music_bottom)
    private ConstraintLayout mConstraintLayout;
    @ViewInject(R.id.music_list)
    private RecyclerView mMusicList;

    @ViewInject(R.id.music_thumb_cover)
    private ImageView mCover;
    @ViewInject(R.id.music_name)
    private TextView mMusicName;
    @ViewInject(R.id.music_artist)
    private TextView mMusicArtist;

    @ViewInject(R.id.music_previous)
    private ImageView musicPrevious;
    @ViewInject(R.id.music_play_pause)
    private ImageView musicPlayOrPause;
    @ViewInject(R.id.music_next)
    private ImageView musicNext;

    @ViewInject(R.id.music_progressBar)
    private ProgressBar mMusicProgress;

    private boolean isPlaying = false;

    private List<Music> musicList;
    private MusicListAdapter adapter;
    private Music currentPlayingMusic;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && currentPlayingMusic != null) {
            Glide.with(this).load(new File(currentPlayingMusic.getmMusicThumbAlbumUri())).into(mCover);
            mMusicName.setText(currentPlayingMusic.getmMusicName());
            mMusicArtist.setText(currentPlayingMusic.getmMusicArtist());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (currentPlayingMusic != null) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = getParentActivity().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE).edit();
            String currentPlayingMusicString = gson.toJson(currentPlayingMusic, currentPlayingMusic.getClass());
            editor.putString(CURRENT_PLAYING_MUSIC_STRING_KEY, currentPlayingMusicString);
            editor.commit();
        }
    }

    @Override
    public Integer getLayoutId() {
        return R.layout.fragment_music_list;
    }

    @Override
    public void onItemClick(View view, int position) {
        currentPlayingMusic = musicList.get(position);
        EventBus.getDefault().post(currentPlayingMusic, "music_play_fragment_show");
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
                ObjectAnimator animator;
                if (isPlaying) {
                    animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 1f, 0f);
                    animator.setDuration(382);
                    animator.start();
                    Glide.with(MusicListFragment.this).load(R.drawable.ic_play_thin).into(musicPlayOrPause);
                    animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 0f, 1f);
                    animator.setDuration(382);
                    animator.start();
                    isPlaying = false;
                } else {
                    animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 1f, 0f);
                    animator.setDuration(382);
                    animator.start();
                    Glide.with(MusicListFragment.this).load(R.drawable.ic_pause_thin).into(musicPlayOrPause);
                    animator = ObjectAnimator.ofFloat(musicPlayOrPause, "alpha", 0f, 1f);
                    animator.setDuration(382);
                    animator.start();
                    isPlaying = true;
                }
                break;
            case R.id.music_bottom:
                if (currentPlayingMusic != null) {
                    EventBus.getDefault().post(currentPlayingMusic, "music_play_fragment_show");
                }
                break;
            case R.id.mainactivity_fab:
                break;
            default:
                break;
        }
    }

    private void initView() {
        mCtl.setTitle("");

        Glide.with(this)
                .load("http://cn.bing.com/az/hprichbg/rb/BadlandsCycle_ZH-CN11688990875_1920x1080.jpg")
                .into(mBackgroundImage);

        initRecyclerView();
        restoreCurrentPlayingMusic();
    }

    private void restoreCurrentPlayingMusic() {
        SharedPreferences pref = getParentActivity().getSharedPreferences(CURRENT_PLAYING_MUSIC, Context.MODE_PRIVATE);
        String json = pref.getString(CURRENT_PLAYING_MUSIC_STRING_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Music music = gson.fromJson(json, Music.class);
            if (music != null) {
                currentPlayingMusic = music;
                Glide.with(this).load(new File(music.getmMusicThumbAlbumUri())).into(mCover);
                mMusicName.setText(music.getmMusicName());
                mMusicArtist.setText(music.getmMusicArtist());
            }
        }
    }

    private void setListener() {
        mConstraintLayout.setOnClickListener(this);
        musicPrevious.setOnClickListener(this);
        musicPlayOrPause.setOnClickListener(this);
        musicNext.setOnClickListener(this);
    }

    private void initRecyclerView() {
        Observable.create(new ObservableOnSubscribe<List<Music>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Music>> emitter) throws Exception {
                List<Music> musicList = scanMusic();
                emitter.onNext(musicList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> s) {
                        musicList = s;
                        adapter = new MusicListAdapter();
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
                        layoutManager.setSmoothScrollbarEnabled(true);
                        layoutManager.setAutoMeasureEnabled(true);
                        adapter.setItemList(s);
                        mMusicList.setLayoutManager(layoutManager);
                        mMusicList.setHasFixedSize(true);
                        mMusicList.setNestedScrollingEnabled(false);
                        adapter.setOnItemClickListener(MusicListFragment.this);
                        mMusicList.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        setListener();
                    }
                });
    }

    private List<Music> scanMusic() {
        List<Music> musicList = new ArrayList<>();
        SimpleDateFormat sim = new SimpleDateFormat("mm:ss");
        ContentResolver resolver = getParentActivity().getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            String name = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            String albumId = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
            String thumbAlbum = getThumbAlbum(albumId);
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
            String duration = sim.format(time);
            String size = Formatter.formatFileSize(getParentActivity(), cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
            musicList.add(new Music(path, name, artist, thumbAlbum, null, duration, size));
        }
        return musicList;
    }

    private String getThumbAlbum(String album_id) {
        ContentResolver resolver = getParentActivity().getContentResolver();
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String thumbnail = MediaStore.Audio.Albums.ALBUM_ART;
        String id = MediaStore.Audio.Albums._ID;
        String str = null;
        Cursor cursor = resolver.query(albumUri, new String[]{thumbnail}, id + "=?", new String[]{album_id}, null);
        while (cursor.moveToNext()) {
            str = cursor.getString(cursor.getColumnIndex(thumbnail));
        }
        return str;
    }

    public static MusicListFragment getInstance() {
        MusicListFragment musicListFragment = new MusicListFragment();
        Bundle bundle = new Bundle();
        musicListFragment.setArguments(bundle);
        return musicListFragment;
    }

}
