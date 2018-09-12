package com.zspirytus.enjoymusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MyViewPagerAdapter;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;

import org.simple.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

/**
 * Fragment:
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_home_page)
public class HomePageFragment extends BaseFragment {

    @ViewInject(R.id.home_page_tab_layout)
    private TabLayout mTabLayout;
    @ViewInject(R.id.home_page_view_pager)
    private ViewPager mViewPager;

    private MyViewPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initData() {
        List<Fragment> fragments = new LinkedList<>();
        fragments.add(AllMusicListFragment.getInstance());
        fragments.add(AlbumMusicListFragment.getInstance());
        fragments.add(ArtistMusicListFragment.getInstance());
        mAdapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
    }

    @Override
    protected void initView() {
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ALL));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ALBUM));
        mTabLayout.addTab(mTabLayout.newTab().setText(Constant.HomePageTabTitle.ARTIST));
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
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
        mViewPager.setOffscreenPageLimit(0);
    }

    public static HomePageFragment getInstance() {
        return new HomePageFragment();
    }

}
