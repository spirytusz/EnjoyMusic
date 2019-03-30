package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.ArtistListAdapter;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
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
 * Fragment 以艺术家名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */
@LayoutIdInject(R.layout.fragment_artist_music_list_layout)
public class ArtistMusicListFragment extends LazyLoadBaseFragment
        implements OnItemClickListener, OnItemLongClickListener,
        ArtistListAdapter.OnMoreInfoBtnClickListener {

    @ViewInject(R.id.artist_music_recycler_view)
    private RecyclerView mArtistMusicRecyclerView;
    @ViewInject(R.id.artist_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.artist_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private ArtistListAdapter mAdapter;
    private MainActivityViewModel mMainViewModel;
    private AlphaInAnimationAdapter mAnimationWrapAdapter;

    @Override
    public void onItemClick(View view, int position) {
        Artist artist = mAdapter.getList().get(position);
        List<Music> musicList = QueryExecutor.findMusicList(artist);
        FilterMusicListFragment fragment = FilterMusicListFragment.getInstance(artist.getArtistName(), musicList, FilterMusicListFragment.ARTIST_FLAG);
        FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    @Override
    public void onLongClick(View view, int position) {
        Artist artist = mAdapter.getList().get(position);
        showDialog(artist);
    }

    @Override
    public void onMoreInfoBtnClick(View v, int position) {
        Artist artist = mAdapter.getList().get(position);
        showDialog(artist);
    }

    @Override
    protected void initData() {
        mAdapter = new ArtistListAdapter();
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
        mArtistMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mArtistMusicRecyclerView.setHasFixedSize(true);
        mArtistMusicRecyclerView.setNestedScrollingEnabled(false);
        mArtistMusicRecyclerView.setAdapter(mAnimationWrapAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel.getArtistList().observe(this, values -> {
            mLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mArtistMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText(R.string.no_artist_tip);
            }
        });
        mMainViewModel.getNewArtist().observe(this, values -> {
            mAdapter.getList().add(0, values);
            mAdapter.notifyItemInserted(0);
        });
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    private void showDialog(Artist artist) {
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(artist.getArtistName(), Constant.MenuTexts.artistMenuTexts);
        dialog.setOnMenuItemClickListener((menuText, pos) -> {
            switch (pos) {
                case 0:
                    List<Music> artistFilterMusicList = QueryExecutor.findMusicList(artist);
                    ForegroundMusicController.getInstance().addToPlayList(artistFilterMusicList);
                    ToastUtil.showToast(MainApplication.getAppContext(), R.string.success);
                    break;
                case 1:
                    SaveSongListDialog saveSongListDialog = new SaveSongListDialog();
                    saveSongListDialog.setOnDialogButtonClickListener(content -> {
                        List<Music> artistMusicList = QueryExecutor.findMusicList(artist);
                        boolean isSuccess = mMainViewModel.refreshSongLists(content, artistMusicList);
                        if (isSuccess) {
                            saveSongListDialog.dismiss();
                        }
                    });
                    FragmentVisibilityManager.getInstance().showDialogFragment(saveSongListDialog);
                    break;
                case 2:
                    List<Music> musicList = QueryExecutor.findMusicList(artist);
                    FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                    FragmentVisibilityManager.getInstance().show(FilterMusicListFragment.getInstance(artist.getArtistName(), musicList, FilterMusicListFragment.ARTIST_FLAG));
                    break;
                case 3:
                    FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                    FragmentVisibilityManager.getInstance().show(FilterAlbumListFragment.getInstance(artist));
                    break;
            }
        });
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    public static ArtistMusicListFragment getInstance() {
        ArtistMusicListFragment instance = new ArtistMusicListFragment();
        return instance;
    }
}
