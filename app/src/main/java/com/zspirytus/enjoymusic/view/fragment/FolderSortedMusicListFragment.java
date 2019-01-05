package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicStateCache;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;

import java.util.List;

@LayoutIdInject(R.layout.fragment_folder_sorted_music_list_layout)
public class FolderSortedMusicListFragment extends LazyLoadBaseFragment
        implements OnRecyclerViewItemClickListener {

    @ViewInject(R.id.file_sorted_music_fragment_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.file_sorted_music_fragment_info_tv)
    private AppCompatTextView mInfoTextView;
    @ViewInject(R.id.file_sorted_music_fragment_recycler_view)
    private RecyclerView mFileSortedMusicRecyclerView;

    private CommonRecyclerViewAdapter<FolderSortedMusic> mAdapter;
    private List<FolderSortedMusic> mFolderSortedMusicList;

    @Override
    protected void initData() {
        mFolderSortedMusicList = ForegroundMusicStateCache.getInstance().getFolderSortedMusicList();
        mAdapter = new CommonRecyclerViewAdapter<FolderSortedMusic>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, FolderSortedMusic folderSortedMusic, int position) {
                Music firstMusicInFolder = folderSortedMusic.getFolderMusicList().get(0);
                String coverPath = firstMusicInFolder.getMusicThumbAlbumCoverPath();
                if (coverPath != null && !coverPath.isEmpty()) {
                    holder.setImagePath(R.id.item_cover, coverPath);
                } else {
                    holder.setImageResource(R.id.item_cover, R.drawable.defalut_cover);
                }
                holder.setText(R.id.item_title, folderSortedMusic.getParentFolderDir());
                holder.setText(R.id.item_sub_title, folderSortedMusic.getFolderName());
                holder.setOnItemClickListener(FolderSortedMusicListFragment.this);
            }
        };
        mAdapter.setList(mFolderSortedMusicList);
    }

    @Override
    protected void initView() {
        mFileSortedMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mFileSortedMusicRecyclerView.setHasFixedSize(true);
        mFileSortedMusicRecyclerView.setNestedScrollingEnabled(false);
        mFileSortedMusicRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
        mLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!mAdapter.getList().isEmpty()) {
                mFileSortedMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No File to show");
            }
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("Error");
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void goBack() {
        long now = System.currentTimeMillis();
        if (now - pressedBackLastTime < 2 * 1000) {
            getParentActivity().finish();
        } else {
            toast("Press back again to quit");
            pressedBackLastTime = now;
        }
    }

    public static FolderSortedMusicListFragment getInstance() {
        return new FolderSortedMusicListFragment();
    }
}
