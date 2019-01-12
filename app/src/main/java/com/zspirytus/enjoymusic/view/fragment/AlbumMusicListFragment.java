package com.zspirytus.enjoymusic.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.ItemSpacingDecoration;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import org.simple.eventbus.EventBus;

import java.util.List;

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

    private CommonRecyclerViewAdapter<Album> mAdapter;
    private List<Album> mAlbumList;

    @Override
    public void onItemClick(View view, int position) {
        String album = mAlbumList.get(position).getAlbumName();
        MusicListDetailFragment fragment = MusicListDetailFragment.getInstance();
        Bundle bundle = new Bundle();
        bundle.putString("album", album);
        fragment.setArguments(bundle);
        EventBus.getDefault().post(fragment, Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    @Override
    protected void initData() {
        mAlbumList = ForegroundMusicStateCache.getInstance().getAlbumList();
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
        mAdapter.setList(mAlbumList);
    }

    @Override
    protected void initView() {
        mAlbumMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(getParentActivity(), 2));
        mAlbumMusicRecyclerView.setAdapter(mAdapter);
        mAlbumMusicRecyclerView.setHasFixedSize(true);
        mAlbumMusicRecyclerView.setNestedScrollingEnabled(false);
        mAlbumMusicRecyclerView.addItemDecoration(new ItemSpacingDecoration(getContext(), 16, 16, 16, 16, 1, 2));
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
        AnimationUtil.ofFloat(mLoadProgressBar, Constant.AnimationProperty.ALPHA, 1f, 0f);
        mLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!mAlbumList.isEmpty()) {
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
