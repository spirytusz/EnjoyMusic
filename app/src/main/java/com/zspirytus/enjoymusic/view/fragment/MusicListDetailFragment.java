package com.zspirytus.enjoymusic.view.fragment;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import java.io.File;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@LayoutIdInject(R.layout.fragment_music_list_detail_layout)
public class MusicListDetailFragment extends BaseFragment {

    @ViewInject(R.id.collapsing_toolbar)
    private CollapsingToolbarLayout mCollapsing;
    @ViewInject(R.id.music_detail_toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.music_cover)
    private AppCompatImageView mCover;
    @ViewInject(R.id.music_detail_recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.fab)
    private FloatingActionButton mFab;
    @ViewInject(R.id.back_btn)
    private AppCompatImageView mBackBtn;

    private CommonRecyclerViewAdapter<Music> mAdapter;

    private String filterAlbum;
    private String filterArtist;

    @Override
    protected void initData() {
        filterAlbum = getArguments().getString("album");
        filterArtist = getArguments().getString("artist");
        mAdapter = new CommonRecyclerViewAdapter<Music>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Music music, int position) {
                holder.setImagePath(R.id.item_cover, music.getMusicThumbAlbumCoverPath());
                holder.setText(R.id.item_title, music.getMusicName());
                holder.setText(R.id.item_sub_title, music.getMusicAlbumName());
            }
        };
    }

    @Override
    protected void initView() {
        ObservableFactory.filterMusic(filterAlbum, filterArtist)
                .subscribe(new SingleObserver<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<Music> musicList) {
                        loadDataIntoView(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    private void loadDataIntoView(List<Music> musicList) {
        if (musicList != null && !musicList.isEmpty()) {
            Music music = musicList.get(0);
            initRecyclerView(musicList);
            if (filterAlbum != null) {
                mToolbar.setTitle(music.getMusicAlbumName());
                mCollapsing.setTitle(music.getMusicAlbumName());
            } else {
                mToolbar.setTitle(music.getMusicArtist());
                mCollapsing.setTitle(music.getMusicArtist());
            }
            File coverFile = MusicCoverFileCache.getInstance().getCoverFile(music.getMusicThumbAlbumCoverPath());
            GlideApp.with(getContext())
                    .load(coverFile)
                    .centerCrop()
                    .into(mCover);
            mBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .remove(MusicListDetailFragment.this)
                            .commitAllowingStateLoss();
                }
            });
        }
    }

    private void initRecyclerView(List<Music> musicList) {
        mAdapter.setList(musicList);
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
    }

    public static MusicListDetailFragment getInstance() {
        return new MusicListDetailFragment();
    }
}
