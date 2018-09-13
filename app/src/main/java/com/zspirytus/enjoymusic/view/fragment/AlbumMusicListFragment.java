package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.GridMusicListAdapter;
import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment 显示以专辑名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_album_music_list)
public class AlbumMusicListFragment extends LazyLoadBaseFragment
        implements GridMusicListAdapter.OnItemClickListener {

    @ViewInject(R.id.album_music_recycler_view)
    private RecyclerView mAlbumMusicRecyclerView;
    @ViewInject(R.id.album_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.album_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private GridMusicListAdapter mAdapter;
    private List<Album> mAlbumList;

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    protected void initData() {
        mAlbumList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        mAlbumMusicRecyclerView.setVisibility(View.GONE);
        mLoadProgressBar.setVisibility(View.VISIBLE);
        mInfoTextView.setVisibility(View.GONE);
        Observable.create(new ObservableOnSubscribe<Music>() {
            @Override
            public void subscribe(ObservableEmitter<Music> emitter) throws Exception {
                List<Music> musicList = AllMusicCache.getInstance().getAllMusicList();
                for (Music music : musicList) {
                    emitter.onNext(music);
                }
                emitter.onComplete();
            }
        }).map(new Function<Music, Album>() {
            @Override
            public Album apply(Music music) throws Exception {
                return new Album(music.getMusicAlbumName(), music.getMusicThumbAlbumCoverPath(), music.getMusicArtist());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Album>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Album album) {
                        if (!mAlbumList.contains(album)) {
                            mAlbumList.add(album);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        playWidgetAnimation(false, true);
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        if (!mAlbumList.isEmpty()) {
                            playWidgetAnimation(true, false);
                            mAdapter = new GridMusicListAdapter();
                            mAdapter.setAlbumList(mAlbumList);
                            mAdapter.setOnItemClickListener(AlbumMusicListFragment.this);
                            final GridLayoutManager layoutManager = new GridLayoutManager(getParentActivity(), 2);
                            layoutManager.setSmoothScrollbarEnabled(true);
                            layoutManager.setAutoMeasureEnabled(true);
                            mAlbumMusicRecyclerView.setLayoutManager(layoutManager);
                            mAlbumMusicRecyclerView.setAdapter(mAdapter);
                            mAlbumMusicRecyclerView.setHasFixedSize(true);
                            mAlbumMusicRecyclerView.setNestedScrollingEnabled(false);
                        } else {
                            playWidgetAnimation(true, true);
                        }
                    }
                });
    }

    private void playWidgetAnimation(boolean isSuccess, boolean isEmpty) {
        AnimationUtil.ofFloat(mLoadProgressBar, Constant.AnimationProperty.ALPHA, 1f, 0f);
        mLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!isEmpty) {
                AnimationUtil.ofFloat(mAlbumMusicRecyclerView, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
                mAlbumMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f);
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No Album");
            }
        } else {
            AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f);
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("Error");
        }
    }

    public static AlbumMusicListFragment getInstance() {
        return new AlbumMusicListFragment();
    }

}
