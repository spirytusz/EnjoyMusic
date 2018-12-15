package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonItemRecyclerViewAdapter;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.receivers.observer.PlayListChangeObserver;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/17.
 */
@LayoutIdInject(R.layout.fragment_play_list_layout)
public class PlayListFragment extends CommonHeaderBaseFragment
        implements OnRecyclerViewItemClickListener, PlayListChangeObserver {

    @ViewInject(R.id.play_list_rv)
    private RecyclerView mPlayListRecyclerView;
    @ViewInject(R.id.play_list_info_tv)
    private AppCompatTextView mInfoTextView;

    private CommonItemRecyclerViewAdapter<Music> mAdapter;
    private List<Music> mPlayList;

    @Override
    protected void initData() {
        mPlayList = new ArrayList<>();
        mAdapter = new CommonItemRecyclerViewAdapter<>(mPlayList);
    }

    @Override
    protected void initView() {
        setNavIconAction(true);
        setTitle(Constant.FragmentName.playListFragmentName);
        mPlayListRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mPlayListRecyclerView.setHasFixedSize(true);
        mPlayListRecyclerView.setNestedScrollingEnabled(false);
        mAdapter.setOnItemClickListener(this);
        mPlayListRecyclerView.setAdapter(mAdapter);
        setupInfoTextView(mPlayList.isEmpty());
    }

    @Override
    protected void registerEvent() {
        ForegroundMusicCache.getInstance().register(this);
    }

    @Override
    protected void unregisterEvent() {
        ForegroundMusicCache.getInstance().unregister(this);
    }

    @Override
    public void onPlayListChange(List<Music> playList) {
        mPlayList = playList;
        setupInfoTextView(playList.isEmpty());
        mAdapter.setList(mPlayList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Music music = mPlayList.get(position);
        ForegroundMusicController.getInstance().play(music);
        EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicPlayFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    private void setupInfoTextView(boolean isPlayListEmpty) {
        if (!isPlayListEmpty) {
            mInfoTextView.setVisibility(View.GONE);
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("No Music in PlayList");
        }
    }

    public static PlayListFragment getInstance() {
        return new PlayListFragment();
    }
}
