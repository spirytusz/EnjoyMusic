package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

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
import com.zspirytus.enjoymusic.utils.PixelsUtil;

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
    private AnimatorSet mAnim;

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
        playShadowAnimator();
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
            playShadowAnimator();
        } else {
            if (mAnim.isRunning()) {
                mAnim.cancel();
            }
            mAppBarLayout.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mAppBarLayout.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
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

    private void initAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mPlayListRecyclerView, "scaleX", 0.9f, 1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mPlayListRecyclerView, "scaleY", 0.9f, 1f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mPlayListRecyclerView, "alpha", 0f, 1f);
        AnimatorSet recyclerViewAnim = new AnimatorSet();
        recyclerViewAnim.playTogether(animator, animator1, animator2);
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mAppBarLayout, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mAppBarLayout, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mStatusBarView, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(mStatusBarView, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        AnimatorSet shadowAnim = new AnimatorSet();
        shadowAnim.playTogether(animator3, animator4, animator5, animator6);
        mAnim = new AnimatorSet();
        mAnim.playSequentially(recyclerViewAnim, shadowAnim);
        mAnim.setInterpolator(new DecelerateInterpolator());
        mAnim.setDuration(618);
    }

    private void playShadowAnimator() {
        if (mAnim == null) {
            initAnim();
        }
        mAnim.start();
    }

    public static PlayListFragment getInstance() {
        return new PlayListFragment();
    }
}
