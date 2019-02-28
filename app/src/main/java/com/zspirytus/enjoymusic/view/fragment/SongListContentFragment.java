package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.SongListContentAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.SongListContentFragmentViewmodel;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

@LayoutIdInject(R.layout.fragment_song_list_content)
public class SongListContentFragment extends CommonHeaderBaseFragment implements OnItemClickListener {

    private static final String SONG_LIST_KEY = "songList";

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private SongListContentFragmentViewmodel mViewModel;
    private HeaderFooterViewWrapAdapter mAdapter;
    private SongListContentAdapter mInnerAdapter;

    @Override
    protected void initData() {
        SongList songList = getArguments().getParcelable(SONG_LIST_KEY);
        mViewModel = ViewModelProviders.of(this).get(SongListContentFragmentViewmodel.class);
        mInnerAdapter = new SongListContentAdapter();
        mInnerAdapter.setList(mViewModel.getMusicListFromgSongList(songList));
        mInnerAdapter.setOnItemClickListener(this);
        mAdapter = new HeaderFooterViewWrapAdapter() {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                ImageLoader.load(holder.getView(R.id.big_music_preview_cover), null, songList.getSongListName());
                holder.setText(R.id.big_music_preview_text, mViewModel.createSpannableString(songList.getSongListName(), songList.getSongsOfThisSongList()));
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
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        mToolbar.setNavigationOnClickListener(v -> goBack());
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));

        mToolbar.setTitle("歌单");
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public int enterAnim() {
        return R.anim.anim_fragment_translate_show_up;
    }

    @Override
    public void onItemClick(View view, int position) {
        Music music = mInnerAdapter.getList().get(position - mAdapter.getHeaderViewCount());
        ForegroundMusicController.getInstance().play(music);
        ForegroundMusicController.getInstance().setPlayList(mInnerAdapter.getList());
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }

    public static SongListContentFragment getInstance(SongList songList) {
        SongListContentFragment fragment = new SongListContentFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SONG_LIST_KEY, songList);
        fragment.setArguments(bundle);
        return fragment;
    }
}
