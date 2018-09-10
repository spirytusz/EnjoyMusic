package com.zspirytus.enjoymusic.view.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.cache.MusicCache;
import com.zspirytus.enjoymusic.cache.finalvalue.FinalValue;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;

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
 * Fragment: 显示本地音乐列表
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_music_list)
public class MusicListFragment extends BaseFragment
        implements MusicListAdapter.OnItemClickListener {

    @ViewInject(R.id.music_list)
    private RecyclerView mMusicList;
    @ViewInject(R.id.music_list_load_progress_bar)
    private ProgressBar mMusicListLoadProgressBar;
    @ViewInject(R.id.music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private List<Music> musicList;
    private MusicListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, int position) {
        Music music = musicList.get(position);
        ForegroundMusicController.getInstance().play(music);
        EventBus.getDefault().post(music, FinalValue.EventBusTag.MUSIC_NAME_SET);
        EventBus.getDefault().post(music, FinalValue.EventBusTag.SHOW_MUSIC_PLAY_FRAGMENT);
    }

    private void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        Observable.create(new ObservableOnSubscribe<List<Music>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Music>> emitter) throws Exception {
                playAnimator(false);
                List<Music> musicList = MusicCache.getInstance().getMusicList();
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
                        musicList = s;
                        adapter = new MusicListAdapter();
                        final LinearLayoutManager layoutManager = new LinearLayoutManager(getParentActivity());
                        layoutManager.setSmoothScrollbarEnabled(true);
                        layoutManager.setAutoMeasureEnabled(true);
                        adapter.setItemList(s);
                        mMusicList.setLayoutManager(layoutManager);
                        mMusicList.setHasFixedSize(true);
                        mMusicList.setNestedScrollingEnabled(false);
                        adapter.setOnItemClickListener(MusicListFragment.this);
                        mMusicList.setAdapter(adapter);
                        EventBus.getDefault().post(!s.isEmpty(), FinalValue.EventBusTag.SET_DFAB_LISTENER);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showInfoTextView(false);
                        mMusicListLoadProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        playAnimator(true);
                    }
                });
    }

    private void playAnimator(boolean isLoadFinish) {
        if (isLoadFinish) {
            ObjectAnimator animatorOfProgressBar = ObjectAnimator.ofFloat(mMusicListLoadProgressBar, "alpha", 1f, 0f);
            animatorOfProgressBar.setDuration(FinalValue.AnimationDuration.SHORT_DURATION);
            ObjectAnimator animatorOfList = ObjectAnimator.ofFloat(mMusicList, "alpha", 0f, 1f);
            animatorOfList.setDuration(FinalValue.AnimationDuration.SHORT_DURATION);
            animatorOfProgressBar.start();
            mMusicListLoadProgressBar.setVisibility(View.GONE);
            animatorOfList.start();
            if (musicList != null && musicList.size() != 0) {
                mMusicList.setVisibility(View.VISIBLE);
            } else {
                showInfoTextView(true);
            }
        } else {
            mMusicList.setVisibility(View.GONE);
            mMusicListLoadProgressBar.setVisibility(View.VISIBLE);
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

    public static MusicListFragment getInstance() {
        MusicListFragment musicListFragment = new MusicListFragment();
        Bundle bundle = new Bundle();
        musicListFragment.setArguments(bundle);
        return musicListFragment;
    }

}
