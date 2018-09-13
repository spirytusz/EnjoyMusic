package com.zspirytus.enjoymusic.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.LinearMusicListAdapter;
import com.zspirytus.enjoymusic.cache.AllMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import org.simple.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Fragment: 显示本地全部音乐列表
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_all_music_list)
public class AllMusicListFragment extends BaseFragment
        implements LinearMusicListAdapter.OnItemClickListener {

    @ViewInject(R.id.all_music_recycler_view)
    private RecyclerView mMusicRecyclerView;
    @ViewInject(R.id.all_music_list_load_progress_bar)
    private ProgressBar mMusicListLoadProgressBar;
    @ViewInject(R.id.all_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private List<Music> mMusicList;
    private LinearMusicListAdapter mMusicRecyclerViewAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, int position) {
        Music music = mMusicList.get(position);
        ForegroundMusicController.getInstance().play(music);
        EventBus.getDefault().post(music, Constant.EventBusTag.MUSIC_NAME_SET);
        EventBus.getDefault().post(music, Constant.EventBusTag.SHOW_MUSIC_PLAY_FRAGMENT);
    }

    @Override
    protected void initView() {
        mMusicRecyclerView.setVisibility(View.GONE);
        mMusicListLoadProgressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    private void initRecyclerView() {
        Observable.create(new ObservableOnSubscribe<List<Music>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Music>> emitter) throws Exception {
                List<Music> musicList = AllMusicCache.getInstance().getAllMusicList();
                emitter.onNext(musicList);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Music>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Music> s) {
                        mMusicList = s;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showInfoTextView(false);
                        mMusicListLoadProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        mMusicRecyclerViewAdapter = new LinearMusicListAdapter(Constant.RecyclerViewItemType.ALL_MUSIC_ITEM_TYPE);
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
                        layoutManager.setSmoothScrollbarEnabled(true);
                        layoutManager.setAutoMeasureEnabled(true);
                        mMusicRecyclerViewAdapter.setAllMusicItemList(mMusicList);
                        mMusicRecyclerView.setLayoutManager(layoutManager);
                        mMusicRecyclerView.setHasFixedSize(true);
                        mMusicRecyclerView.setNestedScrollingEnabled(false);
                        mMusicRecyclerViewAdapter.setOnItemClickListener(AllMusicListFragment.this);
                        mMusicRecyclerView.setAdapter(mMusicRecyclerViewAdapter);
                        playAnimator();
                    }
                });
    }

    private void playAnimator() {
        AnimationUtil.ofFloat(mMusicListLoadProgressBar, Constant.AnimationProperty.ALPHA, 1f, 0f).start();
        mMusicListLoadProgressBar.setVisibility(View.GONE);
        if (mMusicList != null && mMusicList.size() != 0) {
            AnimationUtil.ofFloat(mMusicRecyclerView, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
            mMusicRecyclerView.setVisibility(View.VISIBLE);
        } else {
            showInfoTextView(true);
        }
    }

    private void showInfoTextView(boolean isSuccessAndNoMusic) {
        mInfoTextView.setVisibility(View.VISIBLE);
        if (isSuccessAndNoMusic) {
            mInfoTextView.setText("No music in this device");
        } else {
            mInfoTextView.setText("Error");
        }
    }

    public static AllMusicListFragment getInstance() {
        AllMusicListFragment allMusicListFragment = new AllMusicListFragment();
        Bundle bundle = new Bundle();
        allMusicListFragment.setArguments(bundle);
        return allMusicListFragment;
    }

}
