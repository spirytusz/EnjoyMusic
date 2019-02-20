package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.FilterMusicListFragmentViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
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
    private HeaderFooterViewWrapAdapter mAdapter;
    private MusicListAdapter mInnerAdapter;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(FILTER_MUSIC_LIST, (ArrayList<Music>) mInnerAdapter.getList());
    }

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(FilterMusicListFragmentViewModel.class);
        mViewModel.init();
        List<Music> allMusicList = ViewModelProviders.of(getParentActivity())
                .get(MainActivityViewModel.class)
                .getMusicList().getValue();
        mViewModel.obtainExtra(getArguments(), allMusicList);
        mInnerAdapter = new MusicListAdapter();
        mInnerAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mBackBtn.setOnClickListener(v -> {
            goBack();
        });
        fixNavBarHeight(mRecyclerView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getTitle().observe(this, values -> {
            mTitle.setText(values);
            mAdapter = new HeaderFooterViewWrapAdapter() {
                @Override
                public void convertHeaderView(CommonViewHolder holder, int position) {
                    ImageLoader.load(holder.getView(R.id.big_music_preview_cover), mInnerAdapter.getList().get(0).getMusicThumbAlbumCoverPath(), values);
                    long totalDuration = 0;
                    for (Music music : mInnerAdapter.getList()) {
                        totalDuration += music.getMusicDuration();
                    }
                    @SuppressWarnings("UnnecessaryLocalVariable")
                    String title = values;
                    String countOfMusic = mInnerAdapter.getList().size() + "首曲目";
                    String duration = (totalDuration / 1000 / 60) + "分钟";
                    String content = title + "\n" + countOfMusic + "\n" + duration;
                    SpannableString spannableString = new SpannableString(content);
                    int pointer = 0;
                    spannableString.setSpan(new RelativeSizeSpan(1.6f), 0, title.length(), 0);

                    spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
                    pointer += title.length() + 1;

                    spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), pointer, pointer + countOfMusic.length(), 0);
                    pointer += countOfMusic.length() + 1;

                    spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), pointer, pointer + duration.length(), 0);

                    holder.setText(R.id.big_music_preview_text, spannableString);
                }

                @Override
                public void convertFooterView(CommonViewHolder holder, int position) {
                }
            };
            mAdapter.addHeaderViews(R.layout.item_big_music_preview);
        });
        mViewModel.getMusicList().observe(this, valuse -> {
            mInnerAdapter.setList(valuse);
            mAdapter.wrap(mInnerAdapter);
            mRecyclerView.setAdapter(mAdapter);
        });
        if (savedInstanceState != null) {
            mInnerAdapter.setList(savedInstanceState.getParcelableArrayList(FILTER_MUSIC_LIST));
        }
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public void onItemClick(View view, int position) {
        ForegroundMusicController.getInstance().play(mInnerAdapter.getList().get(position - 1));
        ForegroundMusicController.getInstance().setPlayList(mInnerAdapter.getList());
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
