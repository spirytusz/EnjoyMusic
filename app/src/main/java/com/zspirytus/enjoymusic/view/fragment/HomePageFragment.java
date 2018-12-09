package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CardViewRecyclerViewItemRecyclerViewAdapter;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page)
public class HomePageFragment extends BaseFragment
        implements OnRecyclerViewItemClickListener {

    @ViewInject(R.id.home_page_recycler_view)
    private RecyclerView mHomePageRecyclerView;
    @ViewInject(R.id.home_page_load_progress_bar)
    private ProgressBar mListLoadProgressBar;
    @ViewInject(R.id.home_page_text_view)
    private AppCompatTextView mInfoTextView;

    private HomePageRecyclerViewLoadObserver mRecyclerViewLoadStateObserver;
    private List<HomePageRecyclerViewItem> mItemList;
    private CardViewRecyclerViewItemRecyclerViewAdapter<HomePageRecyclerViewItem> mAdapter;

    @Override
    protected void initData() {
        mItemList = new ArrayList<>();
    }

    @Override
    protected void initView() {
        ObservableFactory.getHomePageRecyclerViewItemsObservable()
                .subscribe(new Observer<HomePageRecyclerViewItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HomePageRecyclerViewItem homePageRecyclerViewItem) {
                        mItemList.add(homePageRecyclerViewItem);
                    }

                    @Override
                    public void onError(Throwable e) {
                        playAnimation(false);
                    }

                    @Override
                    public void onComplete() {
                        setupMusicRecyclerView(mItemList);
                        playAnimation(true);
                        notifyObserverRecyclerViewLoadFinish();
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position != 0) {
            ForegroundMusicController.getInstance().play(mItemList.get(position).getmMusic());
            ForegroundMusicController.getInstance().setPlayList(MusicFilter.NO_FILTER);
        }
    }

    private void setupMusicRecyclerView(List<HomePageRecyclerViewItem> musicList) {
        mAdapter = new CardViewRecyclerViewItemRecyclerViewAdapter<>(musicList);
        mAdapter.setOnItemClickListener(this);
        mHomePageRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManagerWithHeader(getParentActivity(), 2, 2));
        mHomePageRecyclerView.setAdapter(mAdapter);
        mHomePageRecyclerView.setHasFixedSize(true);
        mHomePageRecyclerView.setNestedScrollingEnabled(false);
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
            mRecyclerViewLoadStateObserver.onLoadFinish();
    }

    public void setRecyclerViewLoadStateObserver(HomePageRecyclerViewLoadObserver observer) {
        mRecyclerViewLoadStateObserver = observer;
    }

    public static HomePageFragment getInstance() {
        HomePageFragment instance = new HomePageFragment();
        return instance;
    }
}
