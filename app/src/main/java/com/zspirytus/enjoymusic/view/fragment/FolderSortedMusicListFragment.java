package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

@LayoutIdInject(R.layout.fragment_folder_sorted_music_list_layout)
public class FolderSortedMusicListFragment extends LazyLoadBaseFragment
        implements OnItemClickListener {

    @ViewInject(R.id.file_sorted_music_fragment_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.file_sorted_music_fragment_info_tv)
    private AppCompatTextView mInfoTextView;
    @ViewInject(R.id.file_sorted_music_fragment_recycler_view)
    private RecyclerView mFileSortedMusicRecyclerView;

    private MainActivityViewModel mViewModel;

    private CommonRecyclerViewAdapter<FolderSortedMusic> mAdapter;
    private AlphaInAnimationAdapter mAnimationWrapAdapter;

    @Override
    protected void initData() {
        mAdapter = new CommonRecyclerViewAdapter<FolderSortedMusic>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, FolderSortedMusic folderSortedMusic, int position) {
                Music firstMusicInFolder = folderSortedMusic.getFolderMusicList().get(0);
                String coverPath = firstMusicInFolder.getMusicThumbAlbumCoverPath();
                ImageLoader.load(holder.getView(R.id.item_cover), coverPath, R.drawable.defalut_cover);
                holder.setText(R.id.item_title, folderSortedMusic.getParentFolderDir());
                holder.setText(R.id.item_sub_title, folderSortedMusic.getFolderName());
                holder.setOnItemClickListener(FolderSortedMusicListFragment.this);
            }
        };
        mAnimationWrapAdapter = new AlphaInAnimationAdapter(new SegmentLoadAdapter(mAdapter));
        mAnimationWrapAdapter.setDuration(618);
        mAnimationWrapAdapter.setInterpolator(new DecelerateInterpolator());
        mViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
    }

    @Override
    protected void initView() {
        mFileSortedMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mFileSortedMusicRecyclerView.setHasFixedSize(true);
        mFileSortedMusicRecyclerView.setNestedScrollingEnabled(false);
        mFileSortedMusicRecyclerView.setAdapter(mAnimationWrapAdapter);
    }

    @Override
    public void lazyWrapDataInView() {
        mViewModel.getFolderList().observe(getParentActivity(), (values) -> {
            mLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mFileSortedMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No Folder to show");
            }
        });
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public static FolderSortedMusicListFragment getInstance() {
        return new FolderSortedMusicListFragment();
    }
}
