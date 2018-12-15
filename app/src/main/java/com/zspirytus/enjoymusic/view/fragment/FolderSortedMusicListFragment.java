package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonItemRecyclerViewAdapter;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;

import java.util.List;

@LayoutIdInject(R.layout.fragment_folder_sorted_music_list_layout)
public class FolderSortedMusicListFragment extends LazyLoadBaseFragment implements OnRecyclerViewItemClickListener {

    private static final String TAG = "MusicScanner";

    @ViewInject(R.id.file_sorted_music_fragment_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.file_sorted_music_fragment_info_tv)
    private AppCompatTextView mInfoTextView;
    @ViewInject(R.id.file_sorted_music_fragment_recycler_view)
    private RecyclerView mFileSortedMusicRecyclerView;

    private CommonItemRecyclerViewAdapter<FolderSortedMusic> mAdapter;
    private List<FolderSortedMusic> mFolderSortedMusicList;

    @Override
    protected void initData() {
        mFolderSortedMusicList = ForegroundMusicCache.getInstance().getFolderSortedMusicList();
    }

    @Override
    protected void initView() {
        if (mFolderSortedMusicList != null && !mFolderSortedMusicList.isEmpty()) {
            mAdapter = new CommonItemRecyclerViewAdapter<>(mFolderSortedMusicList);
            mFileSortedMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
            mFileSortedMusicRecyclerView.setHasFixedSize(true);
            mFileSortedMusicRecyclerView.setNestedScrollingEnabled(false);
            mAdapter.setOnItemClickListener(this);
            mFileSortedMusicRecyclerView.setAdapter(mAdapter);
            playAnimation(true);
        } else {
            playAnimation(false);
            mInfoTextView.setText("No Music In Device");
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    private void playAnimation(boolean isSuccess) {
        mLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            mFileSortedMusicRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
        }
    }

    public static FolderSortedMusicListFragment getInstance() {
        return new FolderSortedMusicListFragment();
    }
}
