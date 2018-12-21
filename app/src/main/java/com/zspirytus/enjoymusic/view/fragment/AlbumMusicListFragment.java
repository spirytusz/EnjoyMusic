package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.enjoymusic.adapter.ItemSpacingDecoration;
import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.base.LazyLoadBaseFragment;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.utils.AnimationUtil;

import java.io.File;
import java.util.List;

/**
 * Fragment 显示以专辑名筛选的音乐列表
 * Created by ZSpirytus on 2018/9/12.
 */
// TODO: 2018/9/17 click recyclerview item to navigate to corresponding music list
@LayoutIdInject(R.layout.fragment_album_music_list_layout)
public class AlbumMusicListFragment extends LazyLoadBaseFragment
        implements OnRecyclerViewItemClickListener {

    @ViewInject(R.id.album_music_recycler_view)
    private RecyclerView mAlbumMusicRecyclerView;
    @ViewInject(R.id.album_music_list_load_progress_bar)
    private ProgressBar mLoadProgressBar;
    @ViewInject(R.id.album_music_list_fragment_info_tv)
    private TextView mInfoTextView;

    private CommonRecyclerViewAdapter<Album> mAdapter;
    private List<Album> mAlbumList;

    @Override
    public void onItemClick(View view, int position) {
        String albumName = mAlbumList.get(position).getAlbumName();
        Music targetAlbumFirstMusic = null;
        for (Music music : ForegroundMusicCache.getInstance().getAllMusicList()) {
            if (albumName.equals(music.getMusicAlbumName())) {
                targetAlbumFirstMusic = music;
                break;
            }
        }
        ForegroundMusicController.getInstance().play(targetAlbumFirstMusic);
        ForegroundMusicController.getInstance().setPlayList(new MusicFilter(albumName, null));
    }

    @Override
    protected void initData() {
        mAlbumList = ForegroundMusicCache.getInstance().getAlbumList();
        mAdapter = new CommonRecyclerViewAdapter<Album>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_card_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Album album, int position) {
                File coverFile = MusicCoverFileCache.getInstance().getCoverFile(album.getAlbumCoverPath());
                holder.setImageFile(R.id.item_cover, coverFile);
                holder.setText(R.id.item_title, album.getAlbumName());
                holder.setText(R.id.item_sub_title, album.getArtist());
                holder.setOnItemClickListener(AlbumMusicListFragment.this);
            }
        };
        mAdapter.setList(mAlbumList);
    }

    @Override
    protected void initView() {
        if (mAlbumList != null && !mAlbumList.isEmpty()) {
            playWidgetAnimation(true, false);
            mAlbumMusicRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(getParentActivity(), 2));
            mAlbumMusicRecyclerView.setAdapter(mAdapter);
            mAlbumMusicRecyclerView.setHasFixedSize(true);
            mAlbumMusicRecyclerView.setNestedScrollingEnabled(false);
            mAlbumMusicRecyclerView.addItemDecoration(new ItemSpacingDecoration(8, 8, 8, 8, 1, 2));
        } else {
            playWidgetAnimation(true, true);
        }
    }

    private void playWidgetAnimation(boolean isSuccess, boolean isEmpty) {
        AnimationUtil.ofFloat(mLoadProgressBar, Constant.AnimationProperty.ALPHA, 1f, 0f);
        mLoadProgressBar.setVisibility(View.GONE);
        if (isSuccess) {
            if (!isEmpty) {
                AnimationUtil.ofFloat(mAlbumMusicRecyclerView, Constant.AnimationProperty.ALPHA, 0f, 1f).start();
                mAlbumMusicRecyclerView.setVisibility(View.VISIBLE);
            } else {
                AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f);
                mInfoTextView.setVisibility(View.VISIBLE);
                mInfoTextView.setText("No Album");
            }
        } else {
            AnimationUtil.ofFloat(mInfoTextView, Constant.AnimationProperty.ALPHA, 0f, 1f);
            mInfoTextView.setVisibility(View.VISIBLE);
            mInfoTextView.setText("Error");
        }
    }

    public static AlbumMusicListFragment getInstance() {
        AlbumMusicListFragment instance = new AlbumMusicListFragment();
        return instance;
    }

}
