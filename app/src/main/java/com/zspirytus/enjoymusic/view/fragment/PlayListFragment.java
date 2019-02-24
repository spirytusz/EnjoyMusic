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
import com.zspirytus.enjoymusic.adapter.PlayListAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.PlayListFragmentViewModel;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

import java.util.ArrayList;
import java.util.List;

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

    private PlayListFragmentViewModel mViewModel;
    private PlayListAdapter mAdapter;
    private AnimatorSet mAnim;
    private AnimatorSet mShadowAnim;

    @Override
    protected void initData() {
        mAdapter = new PlayListAdapter();
        mAdapter.setOnItemClickListener(this);
        List<Music> allMusicList = ViewModelProviders.of(getParentActivity())
                .get(MainActivityViewModel.class)
                .getMusicList().getValue();
        mViewModel = ViewModelProviders.of(this).get(PlayListFragmentViewModel.class);
        mViewModel.init(allMusicList);
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mToolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        mPlayListRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mPlayListRecyclerView.setAdapter(mAdapter);
        setupInfoTextView(true);
        mToolbar.setTitle(R.string.play_list_fragment_title);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mToolbar.inflateMenu(R.menu.play_list_menu);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.begin_to_play:
                    if (!mAdapter.getList().isEmpty()) {
                        Music firstMusic = mAdapter.getList().get(0);
                        ForegroundMusicController.getInstance().play(firstMusic);
                    } else {
                        toast("播放列表为空...");
                    }
                    break;
                case R.id.clear_play_list:
                    ForegroundMusicController.getInstance().setPlayList(new ArrayList<>());
                    break;
            }
            return false;
        });
        mToolbar.getOverflowIcon().setTint(getResources().getColor(R.color.black));
        playShadowAnimator();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getPlayList().observe(this, values -> {
            mAdapter.setList(values);
            if (values != null && !values.isEmpty()) {
                mInfoTextView.setVisibility(View.GONE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
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
        FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
        FragmentVisibilityManager.getInstance().show(FragmentFactory.getInstance().get(MusicPlayFragment.class));
    }

    private void setupInfoTextView(boolean isPlayListEmpty) {
        if (!isPlayListEmpty) {
            mInfoTextView.setVisibility(View.GONE);
            mPlayListRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText(R.string.no_music_in_play_list_tip);
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
        mShadowAnim = new AnimatorSet();
        mShadowAnim.playTogether(animator3, animator4, animator5, animator6);
        mAnim = new AnimatorSet();
        mAnim.playSequentially(recyclerViewAnim, mShadowAnim);
        mAnim.setInterpolator(new DecelerateInterpolator());
        mAnim.setDuration(618);
    }

    private void playShadowAnimator() {
        if (mAnim == null || mShadowAnim == null) {
            initAnim();
        }
        if (mAdapter != null && !mAdapter.getList().isEmpty()) {
            mAnim.start();
        } else {
            mShadowAnim.start();
        }
    }

    public static PlayListFragment getInstance() {
        return new PlayListFragment();
    }
}
