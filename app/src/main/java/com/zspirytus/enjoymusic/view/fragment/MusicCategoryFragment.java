package com.zspirytus.enjoymusic.view.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MyViewPagerAdapter;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * Fragment:
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_music_category_home_page)
public class MusicCategoryFragment extends BaseFragment {

    private static final String HOME_PAGE_POSITION_KEY = "homePagePosition";
    private static final int VIEW_PAGER_MAX_HOLD_FRAGMENT_COUNT = 3;

    @ViewInject(R.id.home_page_tab_layout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.home_page_view_pager)
    private ViewPager mViewPager;

    private MyViewPagerAdapter mAdapter;
    private int mCurrentPosition;

    @Override
    protected void initData() {
        List<Fragment> fragments = new LinkedList<>();
        fragments.add(FragmentFactory.getInstance().create(AllMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().create(AlbumMusicListFragment.class));
        fragments.add(FragmentFactory.getInstance().create(ArtistMusicListFragment.class));
        mAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
    }

    @Override
    protected void initView() {
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ALL));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ALBUM));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ARTIST));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    EventBus.getDefault().post(false, Constant.EventBusTag.SET_DFAB_VISIBLE);
                } else {
                    EventBus.getDefault().post(true, Constant.EventBusTag.SET_DFAB_VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(VIEW_PAGER_MAX_HOLD_FRAGMENT_COUNT);
        mViewPager.setCurrentItem(mCurrentPosition, true);
    }

    @Override
    protected void registerEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void unregisterEvent() {
        EventBus.getDefault().unregister(this);
    }

    @Subscriber(tag = Constant.EventBusTag.SET_VIEW_PAGER_CURRENT_ITEM)
    public void setCurrentPosition(int currentPosition) {
        mCurrentPosition = currentPosition;
        if (mViewPager != null) {
            mViewPager.setCurrentItem(currentPosition, true);
        }
    }

    protected static MusicCategoryFragment getInstance() {
        MusicCategoryFragment instance = new MusicCategoryFragment();
        return instance;
    }
}
