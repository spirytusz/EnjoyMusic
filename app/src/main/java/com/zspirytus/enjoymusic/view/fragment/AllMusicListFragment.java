package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;

import org.simple.eventbus.EventBus;

import java.util.List;

/**
 * Fragment: 显示本地全部音乐列表
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_all_music_list_layout)
public class AllMusicListFragment extends LazyLoadBaseFragment
        implements OnRecyclerViewItemClickListener {

    @ViewInject(R.id.all_music_recycler_view)
    private RecyclerView mMusicRecyclerView;
    @ViewInject(R.id.all_music_list_load_progress_bar)
    private ProgressBar mMusicListLoadProgressBar;
    @ViewInject(R.id.all_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private List<Music> mMusicList;
    private CommonRecyclerViewAdapter<Music> mMusicRecyclerViewAdapter;

    @Override
    public void onItemClick(View view, int position) {
        Music music = mMusicList.get(position);
        ForegroundMusicController.getInstance().play(music);
        ForegroundMusicController.getInstance().setPlayList(MusicFilter.NO_FILTER);
        EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicPlayFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    @Override
    protected void initData() {
        mMusicList = ForegroundMusicStateCache.getInstance().getAllMusicList();
        mMusicRecyclerViewAdapter = new CommonRecyclerViewAdapter<Music>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Music music, int position) {
                String coverPath = music.getMusicThumbAlbumCoverPath();
                holder.setImagePath(R.id.item_cover, coverPath);
                holder.setText(R.id.item_title, music.getMusicName());
                holder.setText(R.id.item_sub_title, music.getMusicAlbumName());
                holder.setOnItemClickListener(AllMusicListFragment.this);
            }
        };
        mMusicRecyclerViewAdapter.setList(mMusicList);
    }

    @Override
    protected void initView() {
        mMusicRecyclerView.setVisibility(View.GONE);
        mMusicListLoadProgressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
        mMusicListLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!mMusicRecyclerViewAdapter.getList().isEmpty()) {
                mMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No music in this device");
            }
        } else {
            mInfoTextView.setVisibility(View.GONE);
            mInfoTextView.setText("Error");
        }
    }

    private void initRecyclerView() {
        mMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mMusicRecyclerView.setHasFixedSize(true);
        mMusicRecyclerView.setNestedScrollingEnabled(false);
        mMusicRecyclerView.setAdapter(mMusicRecyclerViewAdapter);
    }

    public static AllMusicListFragment getInstance() {
        AllMusicListFragment allMusicListFragment = new AllMusicListFragment();
        return allMusicListFragment;
    }

}
