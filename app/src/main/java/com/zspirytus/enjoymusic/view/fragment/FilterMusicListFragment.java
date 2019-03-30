package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.FilterMusicListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.FilterMusicListFragmentViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.view.dialog.SaveSongListDialog;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.fragment_filter_music_list_layout)
public class FilterMusicListFragment extends BaseFragment
        implements OnItemClickListener {

    public static final int ALBUM_FLAG = 1;
    public static final int ARTIST_FLAG = 2;
    public static final int FOLDER_FLAG = 3;

    private static final String TITLE_KEY = "title";
    private static final String MUSIC_LIST_EXTRA_KEY = "MusicListExtra";
    private static final String FLAG_KEY = "flag";

    @ViewInject(R.id.music_detail_recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.back_btn)
    private AppCompatImageView mBackBtn;
    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.music_detail_toolbar)
    private Toolbar mToolbar;

    private MainActivityViewModel mMainViewModel;
    private FilterMusicListFragmentViewModel mViewModel;
    private HeaderFooterViewWrapAdapter mAdapter;
    private FilterMusicListAdapter mInnerAdapter;

    @Override
    protected void initData() {
        int flag = getArguments().getInt(FLAG_KEY);
        mViewModel = ViewModelProviders.of(this).get(FilterMusicListFragmentViewModel.class);
        mMainViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
        List<Music> musicList = getArguments().getParcelableArrayList(MUSIC_LIST_EXTRA_KEY);
        mInnerAdapter = new FilterMusicListAdapter(flag);
        mInnerAdapter.setList(musicList);
        mInnerAdapter.setOnItemClickListener(this);
        mAdapter = new HeaderFooterViewWrapAdapter() {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                String path = null;
                if (flag == ALBUM_FLAG) {
                    Album album = QueryExecutor.findAlbum(mInnerAdapter.getList().get(0));
                    path = album.getArtPath();
                } else if (flag == ARTIST_FLAG) {
                    Artist artist = QueryExecutor.findArtist(mInnerAdapter.getList().get(0));
                    ArtistArt artistArt = QueryExecutor.findArtistArt(artist);
                    if (artistArt != null) {
                        path = artistArt.getArtistArt();
                    }
                }
                String title = getArguments().getString(TITLE_KEY);
                ImageLoader.load(holder.getView(R.id.big_music_preview_cover), path, title, new CenterCrop());
                holder.setText(R.id.big_music_preview_text, mViewModel.createSpannableString(mViewModel.getPreviewTitle(title), mInnerAdapter.getList()));
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mAdapter.addHeaderViews(R.layout.item_big_music_preview);
        mAdapter.wrap(mInnerAdapter);
    }

    @Override
    protected void initView() {
        mBackBtn.setOnClickListener(v -> {
            goBack();
        });
        int flag = getArguments().getInt(FLAG_KEY);
        mTitle.setText(mViewModel.getToolbarTitle(flag));
        mToolbar.inflateMenu(R.menu.filter_music_fragment_menu);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_add_to_play_queue:
                    List<Music> musicList = mInnerAdapter.getList();
                    ForegroundMusicController.getInstance().addToPlayList(musicList);
                    return true;
                case R.id.menu_new_song_list:
                    SaveSongListDialog dialog = new SaveSongListDialog();
                    dialog.setOnDialogButtonClickListener(content -> {
                        boolean isSucess = mMainViewModel.refreshSongLists(content, mInnerAdapter.getList());
                        if (isSucess) {
                            dialog.dismiss();
                        }
                    });
                    FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
                    return true;
            }
            return false;
        });
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        fixNavBarHeight(mRecyclerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MUSIC_LIST_EXTRA_KEY, (ArrayList<Music>) mInnerAdapter.getList());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mInnerAdapter.setList(savedInstanceState.getParcelableArrayList(MUSIC_LIST_EXTRA_KEY));
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // 当本Fragment从hide转为show状态后，强制刷新headerView.
        if (!hidden && mAdapter.getItemCount() != 0) {
            mAdapter.notifyItemChanged(0);
        }
    }

    @Override
    public int enterAnim() {
        return R.anim.anim_fragment_translate_show_up;
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        ForegroundMusicController.getInstance().play(mInnerAdapter.getList().get(position - 1));
        ForegroundMusicController.getInstance().setPlayList(mInnerAdapter.getList());
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }

    public static FilterMusicListFragment getInstance(String title, List<Music> musicList, int flag) {
        FilterMusicListFragment fragment = new FilterMusicListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MUSIC_LIST_EXTRA_KEY, (ArrayList<Music>) musicList);
        bundle.putString(TITLE_KEY, title);
        bundle.putInt(FLAG_KEY, flag);
        fragment.setArguments(bundle);
        return fragment;
    }
}
