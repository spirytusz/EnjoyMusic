package com.zspirytus.enjoymusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.ViewInject;

/**
 * Fragment 以艺术家名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */

@LayoutIdInject(R.layout.fragment_artist_music_list)
public class ArtistMusicListFragment extends BaseFragment {

    @ViewInject(R.id.artist_music_recycler_view)
    private RecyclerView mArtistMusicRecyclerView;
    @ViewInject(R.id.artist_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.artist_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void initData() {
        mArtistMusicRecyclerView.setVisibility(View.GONE);
        mLoadProgressBar.setVisibility(View.GONE);
        mInfoTextView.setVisibility(View.VISIBLE);
        mInfoTextView.setText("hhh");
    }

    @Override
    protected void initView() {

    }

    public static ArtistMusicListFragment getInstance() {
        return new ArtistMusicListFragment();
    }
}
