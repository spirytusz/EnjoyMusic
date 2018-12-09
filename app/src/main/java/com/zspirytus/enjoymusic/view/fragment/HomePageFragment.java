package com.zspirytus.enjoymusic.view.fragment;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CardViewRecyclerViewItemRecyclerViewAdapter;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page)
public class HomePageFragment extends BaseFragment
        implements OnRecyclerViewItemClickListener,
        View.OnClickListener {

    @ViewInject(R.id.home_page_recycler_view)
    private RecyclerView mHomePageRecyclerView;
    @ViewInject(R.id.home_page_load_progress_bar)
    private ProgressBar mListLoadProgressBar;
    @ViewInject(R.id.home_page_text_view)
    private AppCompatTextView mInfoTextView;
    @ViewInject(R.id.home_page_rv_header)
    private ConstraintLayout mRecyclerViewHeader;

    private HomePageRecyclerViewLoadObserver mRecyclerViewLoadStateObserver;
    private List<Music> mItemList;
    private CardViewRecyclerViewItemRecyclerViewAdapter<Music> mAdapter;

    @Override
    protected void initView() {
        ObservableFactory.getHomePageRecyclerViewItemsObservable()
                .subscribe(new Observer<List<Music>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> musicList) {
                        mItemList = musicList;
                        setupMusicRecyclerView(musicList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        playAnimation(false);
                    }

                    @Override
                    public void onComplete() {
                        playAnimation(true);
                        mRecyclerViewHeader.setOnClickListener(HomePageFragment.this);
                        notifyObserverRecyclerViewLoadFinish();
                    }
                });
    }

    @Override
    public void onItemClick(View view, int position) {
        ForegroundMusicController.getInstance().play(mItemList.get(position));
        ForegroundMusicController.getInstance().setPlayList(MusicFilter.NO_FILTER);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_page_rv_header:
                break;
        }
    }

    private void setupMusicRecyclerView(List<Music> musicList) {
        mAdapter = new CardViewRecyclerViewItemRecyclerViewAdapter<>(musicList);
        mAdapter.setOnItemClickListener(this);
        mHomePageRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(getParentActivity(), 2));
        mHomePageRecyclerView.setAdapter(mAdapter);
        mHomePageRecyclerView.setHasFixedSize(true);
        mHomePageRecyclerView.setNestedScrollingEnabled(false);
    }

    private void playAnimation(boolean isLoadSuccess) {
        if (isLoadSuccess) {
            mListLoadProgressBar.setVisibility(View.GONE);
            mHomePageRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerViewHeader.setVisibility(View.VISIBLE);
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
