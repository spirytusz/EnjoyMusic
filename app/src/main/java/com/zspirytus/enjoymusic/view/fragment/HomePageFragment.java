package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.ItemSpacingDecoration;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.MusicDataSharedViewModels;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;
import com.zspirytus.enjoymusic.utils.PixelsUtil;
import com.zspirytus.enjoymusic.utils.RandomUtil;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page_layout)
public class HomePageFragment extends CommonHeaderBaseFragment
        implements OnItemClickListener {

    @ViewInject(R.id.home_page_recycler_view)
    private RecyclerView mHomePageRecyclerView;
    @ViewInject(R.id.home_page_load_progress_bar)
    private ProgressBar mListLoadProgressBar;
    @ViewInject(R.id.home_page_text_view)
    private AppCompatTextView mInfoTextView;

    private HomePageRecyclerViewLoadObserver mRecyclerViewLoadStateObserver;

    private MusicDataSharedViewModels mViewModels;
    private volatile CommonRecyclerViewAdapter<Music> mInnerAdapter;
    private volatile HeaderFooterViewWrapAdapter mHeaderWrapAdapter;
    private volatile ScaleInAnimationAdapter mAnimationWrapAdapter;

    @Override
    protected void initData() {
        mInnerAdapter = new CommonRecyclerViewAdapter<Music>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_card_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Music music, int position) {
                String coverPath = music.getMusicThumbAlbumCoverPath();
                ImageLoader.load(holder.getView(R.id.item_cover), coverPath, R.drawable.defalut_cover);
                holder.setText(R.id.item_title, music.getMusicName());
                holder.setText(R.id.item_sub_title, music.getMusicAlbumName());
                holder.setOnItemClickListener(HomePageFragment.this);
            }
        };
        mHeaderWrapAdapter = new HeaderFooterViewWrapAdapter(mInnerAdapter) {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                holder.setOnItemClickListener(R.id.random_play_text, (view) -> {
                    ForegroundMusicController.getInstance().setPlayMode(Constant.PlayMode.RANDOM);
                    List<Music> musicList = mInnerAdapter.getList();
                    Music randomMusic = musicList.get(RandomUtil.rand(musicList.size()));
                    ForegroundMusicController.getInstance().play(randomMusic);
                });
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mHeaderWrapAdapter.addHeaderViews(R.layout.home_page_rv_header);
        mAnimationWrapAdapter = new ScaleInAnimationAdapter(mHeaderWrapAdapter);
        mAnimationWrapAdapter.setDuration(618);
        mAnimationWrapAdapter.setInterpolator(new DecelerateInterpolator());
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
                ).setHeaderViewCount(1).build()
        );
        notifyObserverRecyclerViewLoadFinish();
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        List<Music> musicList = mInnerAdapter.getList();
        Music selectMusic = musicList.get(position - mHeaderWrapAdapter.getHeaderViewCount());
        ForegroundMusicController.getInstance().play(selectMusic);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModels = ViewModelProviders.of(getParentActivity()).get(MusicDataSharedViewModels.class);
        mViewModels.getMusicList().observe(getParentActivity(), (values) -> {
            mListLoadProgressBar.setVisibility(View.GONE);
            if (values != null && !values.isEmpty()) {
                mInnerAdapter.setList(values);
                mAnimationWrapAdapter.notifyDataSetChanged();
                mHomePageRecyclerView.setAdapter(mAnimationWrapAdapter);
                mHomePageRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No music in device!");
                mHomePageRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void notifyObserverRecyclerViewLoadFinish() {
        if (mRecyclerViewLoadStateObserver != null)
            mRecyclerViewLoadStateObserver.onHomePageLoadFinish();
    }

    public void setRecyclerViewLoadStateObserver(HomePageRecyclerViewLoadObserver observer) {
        mRecyclerViewLoadStateObserver = observer;
    }

    public static HomePageFragment getInstance() {
        HomePageFragment instance = new HomePageFragment();
        return instance;
    }
}
