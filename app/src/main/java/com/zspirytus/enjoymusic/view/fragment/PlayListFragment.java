package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import org.simple.eventbus.EventBus;

/**
 * Created by ZSpirytus on 2018/9/17.
 */
@LayoutIdInject(R.layout.fragment_play_list_layout)
public class PlayListFragment extends CommonHeaderBaseFragment
        implements OnItemClickListener {

    @ViewInject(R.id.play_list_rv)
    private RecyclerView mPlayListRecyclerView;
    @ViewInject(R.id.play_list_info_tv)
    private AppCompatTextView mInfoTextView;

    private MusicListAdapter mAdapter;

    @Override
    protected void initData() {
        mAdapter = new MusicListAdapter();
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mToolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        mPlayListRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mPlayListRecyclerView.setAdapter(mAdapter);
        setupInfoTextView(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewModelProviders.of(getParentActivity())
                .get(MainActivityViewModel.class)
                .getPlayList()
                .observe(getParentActivity(), values -> {
                    if (values != null && !values.isEmpty()) {
                        setupInfoTextView(false);
                        mAdapter.setList(values);
                    } else {
                        setupInfoTextView(true);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && !mAdapter.getList().isEmpty()) {
            mPlayListRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_alpha_show));
        }
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        Music music = mAdapter.getList().get(position);
        ForegroundMusicController.getInstance().play(music);
        EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicPlayFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    private void setupInfoTextView(boolean isPlayListEmpty) {
        if (!isPlayListEmpty) {
            mInfoTextView.setVisibility(View.GONE);
            mPlayListRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("No Music in PlayList");
        }
    }

    public static PlayListFragment getInstance() {
        return new PlayListFragment();
    }
}
