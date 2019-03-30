package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.ItemSpacingDecoration;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.AlbumListAdapter;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;
import com.zspirytus.enjoymusic.view.dialog.SaveSongListDialog;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;

/**
 * Fragment 显示以专辑名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_album_music_list_layout)
public class AlbumMusicListFragment extends LazyLoadBaseFragment
        implements OnItemClickListener, OnItemLongClickListener,
        AlbumListAdapter.OnMoreInfoBtnClickListener {

    @ViewInject(R.id.album_music_recycler_view)
    private RecyclerView mAlbumMusicRecyclerView;
    @ViewInject(R.id.album_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.album_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private MainActivityViewModel mMainViewModel;
    private AlbumListAdapter mAdapter;
    private AlphaInAnimationAdapter mAnimationWrapAdapter;

    @Override
    public void onItemClick(View view, int position) {
        Album album = mAdapter.getList().get(position);
        List<Music> musicList = QueryExecutor.findMusicList(album);
        FilterMusicListFragment fragment = FilterMusicListFragment.getInstance(
                album.getAlbumName(),
                musicList,
                FilterMusicListFragment.ALBUM_FLAG
        );
        FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    @Override
    public void onLongClick(View view, int position) {
        Album album = mAdapter.getList().get(position);
        showDialog(album);
    }

    @Override
    public void onMoreInfoBtnClick(View v, int position) {
        Album album = mAdapter.getList().get(position);
        showDialog(album);
    }

    @Override
    protected void initData() {
        mAdapter = new AlbumListAdapter();
        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickLisener(this);
        mAdapter.setOnMoreInfoBtnClickListener(this);
        mAnimationWrapAdapter = new AlphaInAnimationAdapter(new SegmentLoadAdapter(mAdapter));
        mAnimationWrapAdapter.setDuration(618);
        mAnimationWrapAdapter.setInterpolator(new DecelerateInterpolator());
        mMainViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
    }

    @Override
    protected void initView() {
        mAlbumMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(
                getParentActivity(),
                2
        ));
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
        mAlbumMusicRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(
                    Rect outRect,
                    View view,
                    RecyclerView parent,
                    RecyclerView.State state
            ) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (position == 0 || position == 1) {
                    outRect.top = PixelsUtil.dp2px(getContext(), 16);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel.getAlbumList().observe(this, values -> {
            mLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mAlbumMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText(R.string.no_album_tip);
            }
        });
        mMainViewModel.getNewAlbum().observe(this, values -> {
            mAdapter.getList().add(0, values);
            mAdapter.notifyItemInserted(0);
        });
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    private void showDialog(Album album) {
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(
                album.getAlbumName(),
                Constant.MenuTexts.albumMenuTexts
        );
        dialog.setOnMenuItemClickListener((menuText, pos) -> {
                    switch (pos) {
                        case 0:
                            List<Music> albumFilterMusicList = QueryExecutor.findMusicList(album);
                            ForegroundMusicController.getInstance().addToPlayList(
                                    albumFilterMusicList
                            );
                            ToastUtil.showToast(MainApplication.getAppContext(), R.string.success);
                            break;
                        case 1:
                            SaveSongListDialog saveSongListDialog = new SaveSongListDialog();
                            saveSongListDialog.setOnDialogButtonClickListener(content -> {
                                List<Music> albumMusicList = QueryExecutor.findMusicList(album);
                                mMainViewModel.refreshSongLists(content, albumMusicList);
                            });
                            FragmentVisibilityManager.getInstance().showDialogFragment(
                                    saveSongListDialog
                            );
                            break;
                        case 2:
                            List<Music> musicList = QueryExecutor.findMusicList(album);
                            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                            FragmentVisibilityManager.getInstance().show(
                                    FilterMusicListFragment.getInstance(
                                            album.getAlbumName(),
                                            musicList, FilterMusicListFragment.ALBUM_FLAG
                                    )
                            );
                            break;
                        case 3:
                            Artist artist = QueryExecutor.findArtist(album);
                            List<Music> artistFilterMusicList = QueryExecutor.findMusicList(artist);
                            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                            FragmentVisibilityManager.getInstance().show(
                                    FilterMusicListFragment.getInstance(
                                            album.getAlbumName(),
                                            artistFilterMusicList,
                                            FilterMusicListFragment.ALBUM_FLAG
                                    )
                            );
                            break;
                    }
                }
        );
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    public static AlbumMusicListFragment getInstance() {
        return new AlbumMusicListFragment();
    }

}
