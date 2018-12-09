package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewItemRecyclerViewAdapter;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.FragmentFactory;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import org.simple.eventbus.EventBus;

import java.util.List;

/**
 * Fragment: 显示本地全部音乐列表
 * Created by ZSpirytus on 2018/8/2.
 */

@LayoutIdInject(R.layout.fragment_all_music_list)
public class AllMusicListFragment extends LazyLoadBaseFragment
        implements OnRecyclerViewItemClickListener {

    @ViewInject(R.id.all_music_recycler_view)
    private RecyclerView mMusicRecyclerView;
    @ViewInject(R.id.all_music_list_load_progress_bar)
    private ProgressBar mMusicListLoadProgressBar;
    @ViewInject(R.id.all_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private List<Music> mMusicList;
    private CommonRecyclerViewItemRecyclerViewAdapter<Music> mMusicRecyclerViewAdapter;

    @Override
    public void onItemClick(View view, int position) {
        Music music = mMusicList.get(position);
        ForegroundMusicController.getInstance().play(music);
        ForegroundMusicController.getInstance().setPlayList(MusicFilter.NO_FILTER);
        EventBus.getDefault().post(FragmentFactory.getInstance().get(MusicPlayFragment.class), Constant.EventBusTag.SHOW_CAST_FRAGMENT);
    }

    @Override
    protected void initView() {
        mMusicRecyclerView.setVisibility(View.GONE);
        mMusicListLoadProgressBar.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mMusicList = ForegroundMusicCache.getInstance().getAllMusicList();
        mMusicRecyclerViewAdapter = new CommonRecyclerViewItemRecyclerViewAdapter<>(mMusicList);
        mMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getParentActivity()));
        mMusicRecyclerView.setHasFixedSize(true);
        mMusicRecyclerView.setNestedScrollingEnabled(false);
        mMusicRecyclerViewAdapter.setOnItemClickListener(AllMusicListFragment.this);
        mMusicRecyclerView.setAdapter(mMusicRecyclerViewAdapter);
        playAnimator();
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
        return allMusicListFragment;
    }

}
