package com.zspirytus.enjoymusic.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MyViewPagerAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import java.util.LinkedList;
import java.util.List;

/**
 * Fragment:
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_music_category_layout)
public class MusicCategoryFragment extends CommonHeaderBaseFragment {

    private static final int VIEW_PAGER_MAX_HOLD_FRAGMENT_COUNT = 4;

    @ViewInject(R.id.music_category_tab_layout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.music_category_view_pager)
    private ViewPager mViewPager;

    private MyViewPagerAdapter mAdapter;
    private int mCurrentPosition;


    @Override
    protected void initData() {
        List<LazyLoadBaseFragment> fragments = new LinkedList<>();
        fragments.add(FragmentFactory.getInstance().get(AllMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(AlbumMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(ArtistMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().get(FolderSortedMusicListFragment.class));
        mAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
    }

    @Override
    protected void initView() {
        setNavIconAction(true);
        initTabLayout();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.setOffscreenPageLimit(VIEW_PAGER_MAX_HOLD_FRAGMENT_COUNT);
        mViewPager.setCurrentItem(mCurrentPosition, true);
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
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
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ALL));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ALBUM));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ARTIST));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.FOLDER));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static MusicCategoryFragment getInstance() {
        MusicCategoryFragment instance = new MusicCategoryFragment();
        return instance;
    }
}
