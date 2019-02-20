package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.FilterMusicListFragmentViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.fragment_music_list_detail_layout)
public class FilterMusicListFragment extends BaseFragment
        implements OnItemClickListener {

    private static final String EXTRA_KEY = "extra";
    private static final String FILTER_EXTRA_KEY = "filterExtra";
    private static final String MUSIC_LIST_EXTRA_KEY = "MusicListExtra";

    private static final String FILTER_MUSIC_LIST = "filterMusicList";

    @ViewInject(R.id.music_detail_recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.back_btn)
    private AppCompatImageView mBackBtn;
    @ViewInject(R.id.title)
    private TextView mTitle;

    private FilterMusicListFragmentViewModel mViewModel;
    private MusicListAdapter mAdapter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FILTER_MUSIC_LIST, (ArrayList<Music>) mAdapter.getList());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getTitle().observe(this, values -> {
            mTitle.setText(values);
        });
        mViewModel.getMusicList().observe(this, valuse -> {
            mAdapter.setList(valuse);
        });
        if (savedInstanceState != null) {
            mAdapter.setList(savedInstanceState.getParcelableArrayList(FILTER_MUSIC_LIST));
        }
    }

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(FilterMusicListFragmentViewModel.class);
        mViewModel.init();
        List<Music> allMusicList = ViewModelProviders.of(getParentActivity())
                .get(MainActivityViewModel.class)
                .getMusicList().getValue();
        mViewModel.obtainExtra(getArguments(), allMusicList);
        mAdapter = new MusicListAdapter();
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mBackBtn.setOnClickListener(v -> {
            goBack();
        });
        fixNavBarHeight(mRecyclerView);
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        ForegroundMusicController.getInstance().play(mAdapter.getList().get(position));
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }


    public static FilterMusicListFragment getInstance(MusicFilter filter) {
        FilterMusicListFragment fragment = new FilterMusicListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(FILTER_EXTRA_KEY, filter);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static FilterMusicListFragment getInstance(String title, List<Music> musicList) {
        FilterMusicListFragment fragment = new FilterMusicListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(MUSIC_LIST_EXTRA_KEY, (ArrayList<Music>) musicList);
        bundle.putString(EXTRA_KEY, title);
        fragment.setArguments(bundle);
        return fragment;
    }
}
