package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.ItemSpacingDecoration;
import com.zspirytus.basesdk.recyclerview.adapter.SegmentLoadAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicStateCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;
import com.zspirytus.enjoymusic.utils.RandomUtil;

import java.util.List;

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

    private List<Music> mMusicList;

    private volatile CommonRecyclerViewAdapter<Music> mInnerAdapter;
    private volatile HeaderFooterViewWrapAdapter mAdapter;

    @Override
    protected void initData() {
        mMusicList = ForegroundMusicStateCache.getInstance().getAllMusicList();
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
        mInnerAdapter.setList(mMusicList);
        mAdapter = new HeaderFooterViewWrapAdapter(mInnerAdapter) {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                holder.setOnItemClickListener(R.id.random_play_text, (view) -> {
                    ForegroundMusicController.getInstance().setPlayMode(Constant.PlayMode.RANDOM);
                    Music randomMusic = mMusicList.get(RandomUtil.rand(mMusicList.size()));
                    ForegroundMusicController.getInstance().play(randomMusic);
                });
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mAdapter.addHeaderViews(R.layout.home_page_rv_header);
    }

    @Override
    protected void initView() {
        setNavIconAction();
        setupHeaderView();
        mHomePageRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManagerWithHeader(getParentActivity(), 2, 2));
        mHomePageRecyclerView.setAdapter(new SegmentLoadAdapter(mAdapter));
        mHomePageRecyclerView.setHasFixedSize(true);
        mHomePageRecyclerView.setNestedScrollingEnabled(false);
        mHomePageRecyclerView.addItemDecoration(new ItemSpacingDecoration(getContext(), 8, 8, 8, 8, 1, 2));
        notifyObserverRecyclerViewLoadFinish();
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
        mListLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!mInnerAdapter.getList().isEmpty()) {
                mHomePageRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No Music to show");
            }
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("Load HomePage Error");
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Music selectMusic = mMusicList.get(position - mAdapter.getHeaderViewCount());
        ForegroundMusicController.getInstance().play(selectMusic);
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
