package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.enjoymusic.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.enjoymusic.adapter.ItemSpacingDecoration;
import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;
import com.zspirytus.enjoymusic.utils.RandomUtil;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page_layout)
public class HomePageFragment extends CommonHeaderBaseFragment
        implements OnRecyclerViewItemClickListener {

    @ViewInject(R.id.home_page_recycler_view)
    private RecyclerView mHomePageRecyclerView;
    @ViewInject(R.id.home_page_load_progress_bar)
    private ProgressBar mListLoadProgressBar;
    @ViewInject(R.id.home_page_text_view)
    private AppCompatTextView mInfoTextView;

    private HomePageRecyclerViewLoadObserver mRecyclerViewLoadStateObserver;

    private List<Music> mMusicList;

    private CommonRecyclerViewAdapter<Music> mInnerAdapter;
    private HeaderFooterViewWrapAdapter mAdapter;

    @Override
    protected void initData() {
        mMusicList = ForegroundMusicCache.getInstance().getAllMusicList();
        if (mMusicList != null && !mMusicList.isEmpty()) {
            mInnerAdapter = new CommonRecyclerViewAdapter<Music>() {
                @Override
                public int getLayoutId() {
                    return R.layout.item_card_view_type;
                }

                @Override
                public void convert(CommonViewHolder holder, Music music, int position) {
                    File coverFile = MusicCoverFileCache.getInstance().getCoverFile(music.getMusicThumbAlbumCoverPath());
                    holder.setImageFile(R.id.item_cover, coverFile);
                    holder.setText(R.id.item_title, music.getMusicName());
                    holder.setText(R.id.item_sub_title, music.getMusicAlbumName());
                    holder.setOnItemClickListener(HomePageFragment.this);
                }
            };
            mInnerAdapter.setList(mMusicList);
            mAdapter = new HeaderFooterViewWrapAdapter(mInnerAdapter) {
                @Override
                public void convertHeaderView(CommonViewHolder holder, int position) {
                    holder.setOnItemClickListener(R.id.random_play_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ForegroundMusicController.getInstance().setPlayMode(Constant.PlayMode.RANDOM);
                            Music randomMusic = mMusicList.get(RandomUtil.rand(mMusicList.size()));
                            ForegroundMusicController.getInstance().play(randomMusic);
                        }
                    });
                }

                @Override
                public void convertFooterView(CommonViewHolder holder, int position) {
                }
            };
            mAdapter.addHeaderViews(R.layout.home_page_rv_header);
        }
    }

    @Override
    protected void initView() {
        setNavIconAction(true);
        if (mMusicList != null && !mMusicList.isEmpty()) {
            mHomePageRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManagerWithHeader(getParentActivity(), 2, 2));
            mHomePageRecyclerView.setAdapter(mAdapter);
            mHomePageRecyclerView.setHasFixedSize(true);
            mHomePageRecyclerView.setNestedScrollingEnabled(false);
            mHomePageRecyclerView.addItemDecoration(new ItemSpacingDecoration(8, 8, 8, 8, 1, 2));
            playAnimation(true);
            notifyObserverRecyclerViewLoadFinish();
        } else {
            playAnimation(false);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Music selectMusic = mMusicList.get(position);
        ForegroundMusicController.getInstance().play(selectMusic);
    }

    private void playAnimation(boolean isLoadSuccess) {
        if (isLoadSuccess) {
            mListLoadProgressBar.setVisibility(View.GONE);
            mHomePageRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mInfoTextView.setText("Load HomePage Error!");
            mInfoTextView.setVisibility(View.VISIBLE);
        }
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
