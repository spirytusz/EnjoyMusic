package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.HomePageRecyclerViewAdapter;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.receivers.observer.HomePageRecyclerViewLoadObserver;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page)
public class HomePageFragment extends BaseFragment implements HomePageRecyclerViewAdapter.OnParentRVItemClickListener {

    @ViewInject(R.id.home_page_recycler_view)
    private RecyclerView mHomePageRecyclerView;
    @ViewInject(R.id.home_page_load_progress_bar)
    private ProgressBar mListLoadProgressBar;
    @ViewInject(R.id.home_page_text_view)
    private AppCompatTextView mInfoTextView;

    private HomePageRecyclerViewLoadObserver mObserver;
    private List<HomePageRecyclerViewItem> mItemList;
    private HomePageRecyclerViewAdapter mAdapter;

    @Override
    protected void initView() {
        ObservableFactory.getHomePageRecyclerViewItemsObservable()
                .subscribe(new Observer<List<HomePageRecyclerViewItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<HomePageRecyclerViewItem> homePageRecyclerViewItems) {
                        mItemList = homePageRecyclerViewItems;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        playAnimation(false, true);
                    }

                    @Override
                    public void onComplete() {
                        if (!mItemList.isEmpty()) {
                            mHomePageRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
                            mHomePageRecyclerView.setNestedScrollingEnabled(false);
                            mAdapter = new HomePageRecyclerViewAdapter(mItemList);
                            mAdapter.setOnParentRVItemClickListener(HomePageFragment.this);
                            mHomePageRecyclerView.setAdapter(mAdapter);
                            playAnimation(true, false);
                        } else {
                            playAnimation(true, true);
                        }
                        if (mObserver != null) {
                            mObserver.onLoadFinish();
                        }
                    }
                });
    }

    @Override
    public void onParentRVItemClick(final String cardTitle, final int position, final int type) {
        ObservableFactory.getMusicObservableConverterByTypeAndKey(cardTitle, type)
                .subscribe(new Observer<Music>() {

                    List<Music> playList = new ArrayList<>();
                    boolean isFoundSuitableMusicToPlay = false;

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Music music) {
                        if (!isFoundSuitableMusicToPlay && type == 0 && cardTitle.equals(music.getMusicName())) {
                            ForegroundMusicController.getInstance().play(music);
                            isFoundSuitableMusicToPlay = true;
                        } else if (!isFoundSuitableMusicToPlay && type != 0) {
                            ForegroundMusicController.getInstance().play(music);
                            isFoundSuitableMusicToPlay = true;
                        }
                        playList.add(music);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        EventBus.getDefault().post(playList, Constant.EventBusTag.SET_PLAY_LIST);
                        if (type != 0) {
                            EventBus.getDefault().post(FragmentFactory.getInstance().get(PlayListFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                        }
                    }
                });
    }

    public void setRecyclerViewLoadStateObserver(HomePageRecyclerViewLoadObserver observer) {
        mObserver = observer;
    }

    private void playAnimation(boolean isSuccess, boolean isEmpty) {
        mListLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!isEmpty) {
                AnimationUtil.ofFloat(mHomePageRecyclerView, Constant.AnimationProperty.ALPHA, 0f, 1f);
            } else {
                mHomePageRecyclerView.setVisibility(View.GONE);
                AnimationUtil.ofFloat(mHomePageRecyclerView, Constant.AnimationProperty.ALPHA, 1f, 10f);
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("Nothing to show");
                AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f);
            }
        } else {
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("Error");
            AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f);
        }
    }

    public static HomePageFragment getInstance() {
        HomePageFragment instance = new HomePageFragment();
        return instance;
    }
}
