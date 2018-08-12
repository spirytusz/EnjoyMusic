package com.zspirytus.enjoymusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zspirytus.enjoymusic.Interface.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.services.StorageHelper;

import org.simple.eventbus.EventBus;

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

public class MusicListFragment extends BaseFragment
        implements MusicListAdapter.OnItemClickListener {

    @ViewInject(R.id.music_list)
    private RecyclerView mMusicList;
    @ViewInject(R.id.main_activity_toolbar)
    private Toolbar mToolbar;

    private List<Music> musicList;
    private MusicListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public Integer getLayoutId() {
        return R.layout.fragment_music_list;
    }

    @Override
    public void onItemClick(View view, int position) {
        Music music = musicList.get(position);
        EventBus.getDefault().post(music, "set current Playing Music");
    }

    private void initView() {

        initRecyclerView();
    }

    private void initRecyclerView() {
        Observable.create(new ObservableOnSubscribe<List<Music>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Music>> emitter) throws Exception {
                List<Music> musicList = StorageHelper.scanMusic();
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

                    }
                });
    }

    public static MusicListFragment getInstance() {
        MusicListFragment musicListFragment = new MusicListFragment();
        Bundle bundle = new Bundle();
        musicListFragment.setArguments(bundle);
        return musicListFragment;
    }

}
