package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.AboutListAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.AboutFragmentViewModel;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_about_layout)
public class AboutFragment extends CommonHeaderBaseFragment implements OnItemClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private AboutFragmentViewModel mViewModel;
    private AboutListAdapter mAdapter;
    private AnimatorSet mShadowAnim;

    public static AboutFragment getInstance() {
        return new AboutFragment();
    }

    @Override
    protected void initView() {
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mToolbar.setTitle(R.string.about_fragment_title);
        mToolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        playShadowAnimator();
    }

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(AboutFragmentViewModel.class);
        mViewModel.init();
        mViewModel.obtainListItem();
        mAdapter = new AboutListAdapter();
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (mAdapter.getData().get(position).isTitle()) {
                    if (position == 0) {
                        outRect.top = PixelsUtil.dp2px(getContext(), 6);
                    } else {
                        outRect.top = PixelsUtil.dp2px(getContext(), 16);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getListItem().observe(this, values -> {
            mAdapter.setData(values);
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //getParentActivity().setLightStatusIconColor();
            playShadowAnimator();
        } else {
            //getParentActivity().setDefaultStatusIconColor();
            if (mShadowAnim.isRunning()) {
                mShadowAnim.cancel();
            }
            mAppBarLayout.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mAppBarLayout.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
        }
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        String extraInfo = mAdapter.getData().get(position).getExtraInfo();
        if (extraInfo != null) {
            Intent intent = new Intent();
            intent.setData(Uri.parse(extraInfo));
            intent.setAction(Intent.ACTION_VIEW);
            this.startActivity(intent);
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
}
