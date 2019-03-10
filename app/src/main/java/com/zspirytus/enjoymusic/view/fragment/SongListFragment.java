package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.SongListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;

@LayoutIdInject(R.layout.fragment_song_list_layout)
public class SongListFragment extends BaseFragment implements OnItemClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private MainActivityViewModel mViewModel;
    private HeaderFooterViewWrapAdapter mAdapter;
    private SongListAdapter mInnerAdapter;

    @Override
    protected void initData() {
        mInnerAdapter = new SongListAdapter();
        mInnerAdapter.setOnItemClickListener(this);
        mAdapter = new HeaderFooterViewWrapAdapter(mInnerAdapter) {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                holder.setVisibility(R.id.header_title, View.VISIBLE);
                holder.setText(R.id.header_title, R.string.create_new_song_list);
                holder.setImageResource(R.id.img, R.drawable.ic_add_black_48dp);
                holder.setOnItemClickListener(SongListFragment.this);
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mAdapter.addHeaderViews(R.layout.item_song_list);
        mViewModel = ViewModelProviders.of(FragmentVisibilityManager.getInstance().getCurrentFragment().getParentActivity())
                .get(MainActivityViewModel.class);
        mViewModel.applySongLists();
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
            SongList item = mInnerAdapter.getList().get(position - mAdapter.getHeaderViewCount());
            SongListContentFragment fragment = SongListContentFragment.getInstance(item);
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
