package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_settings_layout)
public class SettingsFragment extends CommonHeaderBaseFragment implements OnItemClickListener {

    @ViewInject(R.id.settings_recyclerview)
    private RecyclerView mRecyclerView;

    private CommonRecyclerViewAdapter<String> mAdapter;
    private AnimatorSet mShadowAnim;

    public static SettingsFragment getInstance() {
        SettingsFragment instance = new SettingsFragment();
        return instance;
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mToolbar.setTitle(R.string.settings_fragment_title);
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        playShadowAnimator();
    }

    @Override
    protected void initData() {
        List<String> rvItem = new ArrayList<>();
        rvItem.add("音效");
        mAdapter = new CommonRecyclerViewAdapter<String>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_simple;
            }

            @Override
            public void convert(CommonViewHolder holder, String s, int position) {
                holder.setVisibility(R.id.item_switch, View.GONE);
                holder.setVisibility(R.id.item_image, View.GONE);
                holder.setText(R.id.item_text, s);
                holder.setOnItemClickListener(SettingsFragment.this);
            }
        };
        mAdapter.setList(rvItem);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            AudioEffectFragment fragment = AudioEffectFragment.getInstance();
            FragmentVisibilityManager.getInstance().addToBackStack(this);
            FragmentVisibilityManager.getInstance().show(fragment);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            playShadowAnimator();
        } else {
            if (mShadowAnim.isRunning()) {
                mShadowAnim.cancel();
            }
            mAppBarLayout.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mAppBarLayout.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
        }
    }

    private void initAnim() {
        mShadowAnim = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAppBarLayout, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mAppBarLayout, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mStatusBarView, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mStatusBarView, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        mShadowAnim = new AnimatorSet();
        mShadowAnim.playTogether(animator, animator1, animator2, animator3);
        mShadowAnim.setInterpolator(new DecelerateInterpolator());
        mShadowAnim.setDuration(618);
    }

    private void playShadowAnimator() {
        if (mShadowAnim == null) {
            initAnim();
        }
        mShadowAnim.start();
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
    }
}
