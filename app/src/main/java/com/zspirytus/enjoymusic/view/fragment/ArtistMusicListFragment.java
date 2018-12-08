package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.LinearMusicListAdapter;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment 以艺术家名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */
// TODO: 2018/9/17 click recyclerview to navigate to corresponding music list
@LayoutIdInject(R.layout.fragment_artist_music_list)
public class ArtistMusicListFragment extends LazyLoadBaseFragment
        implements LinearMusicListAdapter.OnItemClickListener {

    @ViewInject(R.id.artist_music_recycler_view)
    private RecyclerView mArtistMusicRecyclerView;
    @ViewInject(R.id.artist_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.artist_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private List<Artist> mArtistList;
    private SparseArray<Integer> mArtistListIndexHolder;
    private LinearMusicListAdapter mAdapter;

    @Override
    protected void initData() {
        mArtistList = new ArrayList<>();
        mArtistListIndexHolder = new SparseArray<>();
    }

    @Override
    protected void initView() {
        mArtistList = ForegroundMusicCache.getInstance().getArtistList();
        mAdapter = new LinearMusicListAdapter(Constant.RecyclerViewItemType.ARTIST_MUSIC_ITEM_TYPE);
        mAdapter.setArtistMusicItemList(mArtistList);
        mArtistMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mArtistMusicRecyclerView.setHasFixedSize(true);
        mArtistMusicRecyclerView.setNestedScrollingEnabled(false);
        mAdapter.setOnItemClickListener(ArtistMusicListFragment.this);
        mArtistMusicRecyclerView.setAdapter(mAdapter);
        if (!mArtistList.isEmpty()) {
            playWidgetAnimation(true, false);
        } else {
            playWidgetAnimation(true, true);
        }
        /*ObservableFactory.getArtistObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Artist>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Artist artist) {
                        int artistHashCode = artist.hashCode();
                        if (!mArtistList.contains(artist)) {
                            mArtistList.add(artist);
                            mArtistListIndexHolder.put(artistHashCode, mArtistList.size() - 1);
                        } else {
                            int index = mArtistListIndexHolder.indexOfKey(artistHashCode);
                            mArtistList.get(index).increaseMusicCount();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        playWidgetAnimation(false, true);
                    }

                    @Override
                    public void onComplete() {
                        mAdapter = new LinearMusicListAdapter(Constant.RecyclerViewItemType.ARTIST_MUSIC_ITEM_TYPE);
                        mAdapter.setArtistMusicItemList(mArtistList);
                        mArtistMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
                        mArtistMusicRecyclerView.setHasFixedSize(true);
                        mArtistMusicRecyclerView.setNestedScrollingEnabled(false);
                        mAdapter.setOnItemClickListener(ArtistMusicListFragment.this);
                        mArtistMusicRecyclerView.setAdapter(mAdapter);
                        if (!mArtistList.isEmpty()) {
                            playWidgetAnimation(true, false);
                        } else {
                            playWidgetAnimation(true, true);
                        }
                    }
                });*/
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void playWidgetAnimation(boolean isSuccess, boolean isEmpty) {
        AnimationUtil.ofFloat(mLoadProgressBar, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
        mLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!isEmpty) {
                AnimationUtil.ofFloat(mArtistMusicRecyclerView, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
            } else {
                AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
                mInfoTextView.setVisibility(View.VISIBLE);
            }
        } else {
            AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
            mInfoTextView.setVisibility(View.VISIBLE);
        }
    }

    public static ArtistMusicListFragment getInstance() {
        ArtistMusicListFragment instance = new ArtistMusicListFragment();
        return instance;
    }
}
