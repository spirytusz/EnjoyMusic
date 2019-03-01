package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.animation.DecelerateInterpolator;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MyViewPagerAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.utils.PixelsUtil;
import com.zspirytus.enjoymusic.view.widget.lazyviewpager.LazyViewPager;

import java.util.LinkedList;
import java.util.List;

/**
 * Fragment:
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_music_category_layout)
public class MusicCategoryFragment extends CommonHeaderBaseFragment {

    private static final int VIEW_PAGER_MAX_HOLD_FRAGMENT_COUNT = 5;
    private static final String CURRENT_POSITION = "currentPosition";

    @ViewInject(R.id.music_category_tab_layout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.music_category_view_pager)
    private LazyViewPager mViewPager;

    private MyViewPagerAdapter mAdapter;
    private int mCurrentPosition;
    private AnimatorSet mAnim;

    @Override
    protected void initData() {
        List<BaseFragment> fragments = new LinkedList<>();
        fragments.add(FragmentFactory.getInstance().get(AllMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(AlbumMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(ArtistMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(FolderListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(SongListFragment.class));
        mAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
    }

    @Override
    protected void initView() {
        mToolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        initTabLayout();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(VIEW_PAGER_MAX_HOLD_FRAGMENT_COUNT);
        mViewPager.setCurrentItem(mCurrentPosition, true);
        setCurrentPosition(getArguments().getInt("currentPosition"));
        playShadowAnimator();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_POSITION, getCurrentPosition());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            setCurrentPosition(savedInstanceState.getInt(CURRENT_POSITION));
        }
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

    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(currentPosition, true);
        }
    }

    public int getCurrentPosition() {
        return mCurrentPosition;
    }

    private void initTabLayout() {
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getText(R.string.all_music)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getText(R.string.album)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getText(R.string.artist)));
        mTabLayout.addTab(mTabLayout.newTab().setText(getResources().getText(R.string.folder)));
        mTabLayout.addTab(mTabLayout.newTab().setText("歌单"));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                /**
                 * ViewPager初始化Adapter后再响应Tab选中事件。
                 * 否则第一次tab.getPosition()只会返回0，
                 * 返回0，NavigationView中的菜单项选中态就不正确.
                 */
                if (mAdapter != null) {
                    mCurrentPosition = tab.getPosition();
                    FragmentVisibilityManager.getInstance().onChildFragmentChange(MusicCategoryFragment.this, mAdapter.getItem(mCurrentPosition));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void playShadowAnimator() {
        if (mAnim == null) {
            initAnim();
        }
        mAnim.start();
    }

    private void initAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAppBarLayout, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mAppBarLayout, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mStatusBarView, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mStatusBarView, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        ObjectAnimator animator4 = ObjectAnimator.ofFloat(mViewPager, "scaleX", 0.9f, 1f);
        ObjectAnimator animator5 = ObjectAnimator.ofFloat(mViewPager, "scaleY", 0.9f, 1f);
        ObjectAnimator animator6 = ObjectAnimator.ofFloat(mViewPager, "alpha", 0f, 1f);
        AnimatorSet viewPagerAnim = new AnimatorSet();
        AnimatorSet shadowAnim = new AnimatorSet();
        viewPagerAnim.playTogether(animator4, animator5, animator6);
        shadowAnim.playTogether(animator, animator1, animator2, animator3);
        mAnim = new AnimatorSet();
        mAnim.playSequentially(viewPagerAnim, shadowAnim);
        mAnim.setInterpolator(new DecelerateInterpolator());
        mAnim.setDuration(618);
    }

    public static MusicCategoryFragment getInstance() {
        return new MusicCategoryFragment();
    }
}
