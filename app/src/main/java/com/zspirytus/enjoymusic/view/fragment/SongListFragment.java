package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.SongListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.SongListFragmentViewModel;
import com.zspirytus.enjoymusic.db.table.Song;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.fragment_song_list_layout)
public class SongListFragment extends BaseFragment implements OnItemClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private SongListFragmentViewModel mViewModel;
    private HeaderFooterViewWrapAdapter mAdapter;
    private SongListAdapter mInnerAdapter;

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(SongListFragmentViewModel.class);
        mViewModel.init();
        mViewModel.applySongList();
        mInnerAdapter = new SongListAdapter();
        mInnerAdapter.setOnItemClickListener(this);
        mAdapter = new HeaderFooterViewWrapAdapter(mInnerAdapter) {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                holder.setVisibility(R.id.header_title, View.VISIBLE);
                holder.setText(R.id.header_title, "添加歌单");
                holder.setImageResource(R.id.img, R.drawable.ic_add_black_48dp);
                holder.setOnItemClickListener(SongListFragment.this);
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mAdapter.addHeaderViews(R.layout.item_song_list);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getSongList().observe(this, values -> {
            mInnerAdapter.setList(values);
            mRecyclerView.setAdapter(mAdapter);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            MusicPickFragment fragment = MusicPickFragment.getInstance();
            fragment.setOnSaveSongListListener(item -> {
                mViewModel.getSongList().getValue().add(item);
            });
            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
            FragmentVisibilityManager.getInstance().show(fragment);
        } else {
            SongList item = mInnerAdapter.getList().get(position - 1);
            List<Music> musicList = new ArrayList<>();
            for (Song song : item.getSongsOfThisSongList()) {
                musicList.add(song.create());
            }
            FilterMusicListFragment fragment = FilterMusicListFragment.getInstance(item.getSongListName(), musicList);
            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
            FragmentVisibilityManager.getInstance().show(fragment);
        }
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }


    public static SongListFragment getIntance() {
        return new SongListFragment();
    }
}