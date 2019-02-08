package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.ItemSpacingDecoration;
import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

import org.simple.eventbus.EventBus;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

/**
 * Fragment 显示以专辑名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_album_music_list_layout)
public class AlbumMusicListFragment extends LazyLoadBaseFragment
        implements OnItemClickListener {

    @ViewInject(R.id.album_music_recycler_view)
    private RecyclerView mAlbumMusicRecyclerView;
    @ViewInject(R.id.album_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.album_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private MainActivityViewModel mViewModel;

    private CommonRecyclerViewAdapter<Album> mAdapter;
    private AlphaInAnimationAdapter mAnimationWrapAdapter;

    @Override
    public void onItemClick(View view, int position) {
        String album = mAdapter.getList().get(position).getAlbumName();
        MusicListDetailFragment fragment = MusicListDetailFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("album", album);
        fragment.setArguments(bundle);
        EventBus.getDefault().post(fragment, Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    @Override
    protected void initData() {
        mAdapter = new CommonRecyclerViewAdapter<Album>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_card_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Album album, int position) {
                String coverPath = album.getAlbumCoverPath();
                ImageLoader.load(holder.getView(R.id.item_cover), coverPath, R.drawable.defalut_cover);
                holder.setText(R.id.item_title, album.getAlbumName());
                holder.setText(R.id.item_sub_title, album.getArtist());
                holder.setOnItemClickListener(AlbumMusicListFragment.this);
            }
        };
        mAnimationWrapAdapter = new AlphaInAnimationAdapter(new SegmentLoadAdapter(mAdapter));
        mAnimationWrapAdapter.setDuration(618);
        mAnimationWrapAdapter.setInterpolator(new DecelerateInterpolator());
        mViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
    }

    @Override
    protected void initView() {
        mAlbumMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(getParentActivity(), 2));
        mAlbumMusicRecyclerView.setAdapter(mAnimationWrapAdapter);
        mAlbumMusicRecyclerView.setHasFixedSize(true);
        mAlbumMusicRecyclerView.setNestedScrollingEnabled(false);
        mAlbumMusicRecyclerView.addItemDecoration(
                new ItemSpacingDecoration.Builder(
                        PixelsUtil.dp2px(getContext(), 16),
                        PixelsUtil.dp2px(getContext(), 16),
                        PixelsUtil.dp2px(getContext(), 16),
                        PixelsUtil.dp2px(getContext(), 16)
                ).build()
        );
    }

    @Override
    protected void lazyWrapDataInView() {
        mViewModel.getAlbumList().observe(getParentActivity(), (values) -> {
            mLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mAlbumMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No Album");
            }
        });
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    public static AlbumMusicListFragment getInstance() {
        AlbumMusicListFragment instance = new AlbumMusicListFragment();
        return instance;
    }

}
