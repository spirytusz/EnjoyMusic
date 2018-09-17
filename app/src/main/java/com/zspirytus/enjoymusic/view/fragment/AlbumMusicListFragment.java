package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.GridMusicListAdapter;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment 显示以专辑名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */
// TODO: 2018/9/17 click recyclerview item to navigate to corresponding music list
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
        ObservableFactory.getAlbumObservable()
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
                        e.printStackTrace();
                        playWidgetAnimation(false, true);
                    }

                    @Override
                    public void onComplete() {
                        if (!mAlbumList.isEmpty()) {
                            playWidgetAnimation(true, false);
                            mAdapter = new GridMusicListAdapter();
                            mAdapter.setAlbumList(mAlbumList);
                            mAdapter.setOnItemClickListener(AlbumMusicListFragment.this);
                            mAlbumMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(getParentActivity(), 2));
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
        AlbumMusicListFragment instance = new AlbumMusicListFragment();
        return instance;
    }

}
