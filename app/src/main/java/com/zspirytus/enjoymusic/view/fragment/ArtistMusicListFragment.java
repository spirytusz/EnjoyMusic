package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.MusicDataSharedViewModels;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import org.simple.eventbus.EventBus;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

/**
 * Fragment 以艺术家名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */
// TODO: 2018/9/17 click recyclerview to navigate to corresponding music list
@LayoutIdInject(R.layout.fragment_artist_music_list_layout)
public class ArtistMusicListFragment extends LazyLoadBaseFragment
        implements OnItemClickListener {

    @ViewInject(R.id.artist_music_recycler_view)
    private RecyclerView mArtistMusicRecyclerView;
    @ViewInject(R.id.artist_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.artist_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private MusicDataSharedViewModels mViewModel;

    private CommonRecyclerViewAdapter<Artist> mAdapter;
    private AlphaInAnimationAdapter mAnimationWrapAdapter;

    @Override
    public void onItemClick(View view, int position) {
        String artist = mAdapter.getList().get(position).getArtistName();
        MusicListDetailFragment fragment = MusicListDetailFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("artist", artist);
        fragment.setArguments(bundle);
        EventBus.getDefault().post(fragment, Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    @Override
    protected void initData() {
        mAdapter = new CommonRecyclerViewAdapter<Artist>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Artist artist, int position) {
                holder.setImageResource(R.id.item_cover, R.drawable.defalut_cover);
                holder.setText(R.id.item_title, artist.getArtistName());
                holder.setText(R.id.item_sub_title, artist.getNumberOfAlbums() + " 首歌曲");
                holder.setOnItemClickListener(ArtistMusicListFragment.this);
            }
        };
        mAnimationWrapAdapter = new AlphaInAnimationAdapter(new SegmentLoadAdapter(mAdapter));
        mAnimationWrapAdapter.setDuration(618);
        mAnimationWrapAdapter.setInterpolator(new DecelerateInterpolator());
        mViewModel = ViewModelProviders.of(getParentActivity()).get(MusicDataSharedViewModels.class);
    }

    @Override
    protected void initView() {
        mArtistMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mArtistMusicRecyclerView.setHasFixedSize(true);
        mArtistMusicRecyclerView.setNestedScrollingEnabled(false);
        mArtistMusicRecyclerView.setAdapter(mAnimationWrapAdapter);
    }

    @Override
    protected void lazyWrapDataInView() {
        mViewModel.getArtistList().observe(getParentActivity(), (values) -> {
            mLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mArtistMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No Artist to Show");
            }
        });
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    public static ArtistMusicListFragment getInstance() {
        ArtistMusicListFragment instance = new ArtistMusicListFragment();
        return instance;
    }
}
