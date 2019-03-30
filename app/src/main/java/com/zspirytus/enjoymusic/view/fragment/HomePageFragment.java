package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.ItemSpacingDecoration;
import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.HomePageListAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.cache.viewmodels.HomePageFragmentViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page_layout)
public class HomePageFragment extends CommonHeaderBaseFragment
        implements OnItemClickListener, AppBarLayout.OnOffsetChangedListener {

    @ViewInject(R.id.home_page_recycler_view)
    private RecyclerView mHomePageRecyclerView;
    @ViewInject(R.id.home_page_load_progress_bar)
    private ProgressBar mListLoadProgressBar;
    @ViewInject(R.id.home_page_text_view)
    private AppCompatTextView mInfoTextView;
    @ViewInject(R.id.search_btn)
    private ImageView mSearchBtn;

    @ViewInject(R.id.bg)
    private ImageView mHomePageDisplayImg;

    private SparseIntArray mItemHeightCache;

    private volatile HomePageListAdapter mInnerAdapter;
    private volatile HeaderFooterViewWrapAdapter mHeaderWrapAdapter;
    private ScaleInAnimationAdapter mAnimationAdapter;
    private MainActivityViewModel mMainViewModel;
    private HomePageFragmentViewModel mViewModel;

    @Override
    protected void initData() {
        mInnerAdapter = new HomePageListAdapter();
        mInnerAdapter.setOnItemClickListener(this);
        mHeaderWrapAdapter = new HeaderFooterViewWrapAdapter(mInnerAdapter) {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                holder.setOnItemClickListener(R.id.random_play_text, (view) -> {
                    ForegroundMusicController.getInstance().setPlayMode(Constant.PlayMode.RANDOM);
                    List<Music> musicList = mInnerAdapter.getList();
                    mViewModel.setPlayList(musicList);
                });
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mHeaderWrapAdapter.addHeaderViews(R.layout.home_page_rv_header);
        mAnimationAdapter = new ScaleInAnimationAdapter(mHeaderWrapAdapter);
        mAnimationAdapter.setInterpolator(new DecelerateInterpolator());
        mAnimationAdapter.setDuration(618);
        mItemHeightCache = new SparseIntArray();
        mViewModel = ViewModelProviders.of(this).get(HomePageFragmentViewModel.class);
        mViewModel.init();
        mMainViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
    }

    @Override
    protected void initView() {
        mHomePageRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManagerWithHeader(getParentActivity(), 2, 2));
        mHomePageRecyclerView.addItemDecoration(
                new ItemSpacingDecoration.Builder(
                        PixelsUtil.dp2px(getContext(), 12),
                        PixelsUtil.dp2px(getContext(), 12),
                        PixelsUtil.dp2px(getContext(), 12),
                        PixelsUtil.dp2px(getContext(), 12)
                ).setHeaderViewCount(1)
                        .setMarginTop(PixelsUtil.dp2px(getContext(), 200))
                        .build()
        );
        mHomePageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int scrollY = computeHomeDisPlayImgTranslationY(recyclerView);
                float translationY = -scrollY / 2;
                mHomePageDisplayImg.setTranslationY(translationY);
                playScrollAnimation(scrollY);
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(this);
        mSearchBtn.setOnClickListener(v -> {
            FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
            FragmentVisibilityManager.getInstance().show(new SearchFragment());
        });
        getParentActivity().setDefaultStatusIconColor();
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        List<Music> musicList = mInnerAdapter.getList();
        int realPos = position - mHeaderWrapAdapter.getHeaderViewCount();
        Music selectMusic = musicList.get(realPos);
        List<Music> playQueue = new ArrayList<>();
        playQueue.addAll(musicList.subList(realPos, musicList.size()));
        playQueue.addAll(musicList.subList(0, realPos));
        ForegroundMusicController.getInstance().play(selectMusic);
        ForegroundMusicController.getInstance().setPlayList(playQueue);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel.getMusicList().observe(this, values -> {
            mViewModel.applyMusicList();
        });
        mViewModel.getPlayListFirstMusic().observe(
                this,
                values -> ForegroundMusicController.getInstance().play(values)
        );
        mViewModel.getNewMusic().observe(this, values -> {
            mInnerAdapter.getList().add(0, values);
            mInnerAdapter.getList().remove(mInnerAdapter.getList().size() - 1);
            //noinspection PointlessArithmeticExpression
            mAnimationAdapter.notifyItemInserted(mHeaderWrapAdapter.getHeaderViewCount() + 0);
            mAnimationAdapter.notifyItemRemoved(mInnerAdapter.getList().size() + mHeaderWrapAdapter.getHeaderViewCount());
        });
        mViewModel.getMusicList().observe(this, this::loadDataToView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            getParentActivity().setDefaultStatusIconColor();
            mHomePageRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_alpha_show));
        } else {
            getParentActivity().setLightStatusIconColor();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    private void loadDataToView(List<Music> values) {
        mListLoadProgressBar.setVisibility(View.GONE);
        if (values != null && !values.isEmpty()) {
            values = values.subList(0, 30 > values.size() ? values.size() : 30);
            mInnerAdapter.setList(values);
            mHomePageRecyclerView.setAdapter(mAnimationAdapter);
            mAnimationAdapter.notifyDataSetChanged();
            mHomePageDisplayImg.post(() -> {
                mHomePageDisplayImg.setVisibility(View.VISIBLE);
                mHomePageDisplayImg.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_alpha_show));
            });
            mHomePageRecyclerView.postDelayed(() -> {
                mHomePageRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_alpha_show));
                mHomePageRecyclerView.setVisibility(View.VISIBLE);
            }, 300);
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText(R.string.no_music_in_device);
            mHomePageRecyclerView.setVisibility(View.GONE);
            mToolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
            mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
            mToolbar.setTitle(R.string.app_name);
            getParentActivity().setLightStatusIconColor();
        }
    }

    private int computeHomeDisPlayImgTranslationY(RecyclerView recyclerView) {
        int firstPos = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int itemType = recyclerView.getAdapter().getItemViewType(firstPos);
        int height = mItemHeightCache.get(itemType, -1);
        GridLayoutManager manager = (GridLayoutManager) mHomePageRecyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        View item = manager.findViewByPosition(firstPos);
        if (height == -1) {
            height = manager.findViewByPosition(firstPos).getHeight();
            mItemHeightCache.put(itemType, height);
        }
        int previousTotalHeight = 0;
        for (int i = 0; i < firstPos; i++) {
            if (i == 0 || (i - 1) % 2 == 0) {
                previousTotalHeight += mItemHeightCache.get(adapter.getItemViewType(i));
            }
        }
        previousTotalHeight += -item.getTop() + PixelsUtil.dp2px(getContext(), 236);
        return previousTotalHeight;
    }

    private void playScrollAnimation(int scrollY) {
        // TODO: 22/01/2019 监听CollapsingToolbarLayout展开状态（正在展开状态）来设置动画效果，阴影效果.
        if (scrollY < PixelsUtil.dp2px(getContext(), 56)) {
            mStatusBarView.getBackground().setAlpha(0);
            mToolbar.getBackground().setAlpha(0);
            mAppBarLayout.getBackground().setAlpha(0);
            mToolbar.setTitleTextColor(getResources().getColor(R.color.transparent));
            Drawable drawable = mToolbar.getNavigationIcon();
            if (drawable != null) {
                drawable.setTint(0xFFFFFFFF);
            }
            mSearchBtn.getDrawable().setTint(0xFFFFFFFF);
            mAppBarLayout.setTranslationZ(0f);
            mAppBarLayout.setElevation(0f);
            mStatusBarView.setTranslationZ(0f);
            mStatusBarView.setElevation(0f);
            getParentActivity().setDefaultStatusIconColor();
        } else {
            mStatusBarView.getBackground().setAlpha(1);
            mToolbar.getBackground().setAlpha(1);
            mAppBarLayout.getBackground().setAlpha(1);
            mStatusBarView.setBackgroundColor(getResources().getColor(R.color.white));
            mToolbar.setBackgroundColor(getResources().getColor(R.color.white));
            mAppBarLayout.setBackgroundColor(getResources().getColor(R.color.white));
            mToolbar.setTitleTextColor(0xFF4C4E4A);
            mToolbar.setTitle(R.string.nav_text_home_page);
            Drawable drawable = mToolbar.getNavigationIcon();
            if (drawable != null) {
                drawable.setTint(0xFF000000);
            }
            mSearchBtn.getDrawable().setTint(0xFF000000);
            mAppBarLayout.setTranslationZ(PixelsUtil.dp2px(getContext(), 4));
            // CollapsingLayout未展开
            if (mAppBarLayout.getBottom() == mStatusBarView.getHeight()) {
                mStatusBarView.setTranslationZ(PixelsUtil.dp2px(getContext(), 5));
            } else {
                mStatusBarView.setTranslationZ(PixelsUtil.dp2px(getContext(), 4));
            }
            mAppBarLayout.setElevation(PixelsUtil.dp2px(getContext(), 6));
            mStatusBarView.setElevation(PixelsUtil.dp2px(getContext(), 6));
            getParentActivity().setLightStatusIconColor();
        }
    }

    public static HomePageFragment getInstance() {
        HomePageFragment instance = new HomePageFragment();
        return instance;
    }
}
