package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

@LayoutIdInject(R.layout.fragment_music_list_detail_layout)
public class MusicListDetailFragment extends BaseFragment
        implements OnItemClickListener {

    private static final String FILTER_MUSIC_LIST = "filterMusicList";

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
    @ViewInject(R.id.appBarLayout)
    private AppBarLayout mAppBarLayout;

    private MusicListAdapter mAdapter;
    private ArrayList<Music> mFilterMusicList;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FILTER_MUSIC_LIST, mFilterMusicList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mFilterMusicList = savedInstanceState.getParcelableArrayList(FILTER_MUSIC_LIST);
            loadDataIntoView();
        }
    }

    @Override
    protected void initData() {
        String filterAlbum = getArguments().getString("album");
        String filterArtist = getArguments().getString("artist");
        MainActivityViewModel viewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
        if (viewModel.getMusicList().getValue() == null)
            return;
        ObservableFactory.filterMusic(viewModel.getMusicList().getValue(), filterAlbum, filterArtist)
                .subscribe(new SingleObserver<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<Music> musicList) {
                        mFilterMusicList = (ArrayList<Music>) musicList;
                        loadDataIntoView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mAppBarLayout.addOnOffsetChangedListener(((appBarLayout, verticalOffset) -> {
            if (PixelsUtil.px2dp(getContext(), verticalOffset) <= -135) {
                mBackBtn.getDrawable().setTint(getResources().getColor(R.color.black));
            } else {
                mBackBtn.getDrawable().setTint(getResources().getColor(R.color.white));
            }
        }));
        mAdapter = new MusicListAdapter();
        mAdapter.setOnItemClickListener(this);
        mFab.setOnClickListener(v -> {
            Music firstMusic = mAdapter.getList().get(0);
            ForegroundMusicController.getInstance().play(firstMusic);
            String filterAlbum = getArguments().getString("album");
            String filterArtist = getArguments().getString("artist");
            MusicFilter filter = new MusicFilter(filterAlbum, filterArtist);
            ForegroundMusicController.getInstance().setPlayList(filter);
        });
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        String filterAlbum = getArguments().getString("album");
        String filterArtist = getArguments().getString("artist");
        MusicFilter filter = new MusicFilter(filterAlbum, filterArtist);
        Music selectedMusic = mAdapter.getList().get(position);
        ForegroundMusicController.getInstance().play(selectedMusic);
        ForegroundMusicController.getInstance().setPlayList(filter);
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }

    private void loadDataIntoView() {
        if (mFilterMusicList != null && !mFilterMusicList.isEmpty()) {
            Music music = mFilterMusicList.get(0);
            initRecyclerView(mFilterMusicList);
            if (getArguments().getString("album") != null) {
                mToolbar.setTitle(music.getMusicAlbumName());
                mCollapsing.setTitle(music.getMusicAlbumName());
            } else {
                mToolbar.setTitle(music.getMusicArtist());
                mCollapsing.setTitle(music.getMusicArtist());
            }
            mCollapsing.setExpandedTitleColor(getResources().getColor(R.color.white));
            mCollapsing.setCollapsedTitleTextColor(getResources().getColor(R.color.black));
            ImageLoader.load(mCover, R.drawable.defalut_cover, new CenterCrop());
            for (Music exitCoverMusic : mFilterMusicList) {
                String path = exitCoverMusic.getMusicThumbAlbumCoverPath();
                if (path != null && !path.isEmpty()) {
                    ImageLoader.load(mCover, path, R.drawable.defalut_cover, new CenterCrop());
                    break;
                }
            }
            mBackBtn.setOnClickListener(v -> goBack());
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
