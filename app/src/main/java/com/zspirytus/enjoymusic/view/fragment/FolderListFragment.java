package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.FolderListAdapter;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

@LayoutIdInject(R.layout.fragment_folder_sorted_music_list_layout)
public class FolderListFragment extends LazyLoadBaseFragment
        implements OnItemClickListener {

    @ViewInject(R.id.file_sorted_music_fragment_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.file_sorted_music_fragment_info_tv)
    private AppCompatTextView mInfoTextView;
    @ViewInject(R.id.file_sorted_music_fragment_recycler_view)
    private RecyclerView mFileSortedMusicRecyclerView;

    private FolderListAdapter mAdapter;
    private AlphaInAnimationAdapter mAnimationWrapAdapter;

    @Override
    protected void initData() {
        mAdapter = new FolderListAdapter();
        mAdapter.setOnItemClickListener(this);
        mAnimationWrapAdapter = new AlphaInAnimationAdapter(new SegmentLoadAdapter(mAdapter));
        mAnimationWrapAdapter.setDuration(618);
        mAnimationWrapAdapter.setInterpolator(new DecelerateInterpolator());
    }

    @Override
    protected void initView() {
        mFileSortedMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mFileSortedMusicRecyclerView.setHasFixedSize(true);
        mFileSortedMusicRecyclerView.setNestedScrollingEnabled(false);
        mFileSortedMusicRecyclerView.setAdapter(mAnimationWrapAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviders.of(getParentActivity())
                .get(MainActivityViewModel.class)
                .getFolderList().observe(getParentActivity(), (values) -> {
            mLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mFileSortedMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText(R.string.no_folder_tip);
            }
        });
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    @Override
    public void onItemClick(View view, int position) {
        Folder folder = mAdapter.getList().get(position);
        List<Music> musicList = QueryExecutor.findMusicList(folder);
        FilterMusicListFragment fragment = FilterMusicListFragment.getInstance(folder.getFolderName(), musicList, FilterMusicListFragment.FOLDER_FLAG);
        FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    public static FolderListFragment getInstance() {
        return new FolderListFragment();
    }
}
