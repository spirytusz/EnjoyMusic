package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.HomePageRecyclerViewAdapter;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.MusicPlayOrderManager;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.factory.ObservableFactory;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    private List<HomePageRecyclerViewItem> mItemList;
    private HomePageRecyclerViewAdapter mAdapter;

    @Override
    protected void initView() {
        ObservableFactory.getHomePageRecyclerViewItemsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                    }
                });
    }

    @Override
    public void onParentRVItemClick(String cardTitle, final int position, int type) {
        ObservableFactory.getMusicObservableConverterByTypeAndKey(cardTitle, type)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Music>() {
                    List<Music> playList = new ArrayList<>();

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Music music) {
                        playList.add(music);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        MusicPlayOrderManager.getInstance().setPlayList(playList);
                        EventBus.getDefault().post(playList, Constant.EventBusTag.SET_PLAY_LIST);
                        EventBus.getDefault().post(FragmentFactory.getInstance().get(PlayListFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
                    }
                });
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
