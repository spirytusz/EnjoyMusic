package com.zspirytus.enjoymusic.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.adapter.HeaderFooterViewWrapAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

import java.util.ArrayList;

@LayoutIdInject(R.layout.fragment_filter_album_list_layout)
public class FilterAlbumListFragment extends BaseFragment {

    private static final String ARTIST_KEY = "artist";
    private static final String ALBUM_LIST_KEY = "albumList";

    @ViewInject(R.id.back_btn)
    private ImageView mBackBtn;
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    private CommonRecyclerViewAdapter<Album> mInnerAdapter;
    private HeaderFooterViewWrapAdapter mAdapter;

    @Override
    protected void initData() {
        Artist artist = getArguments().getParcelable(ARTIST_KEY);
        mInnerAdapter = new CommonRecyclerViewAdapter<Album>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, Album album, int position) {
                ImageLoader.load(holder.getView(R.id.item_cover), album.getAlbumArt(), album.getAlbumName());
                holder.setText(R.id.item_title, album.getAlbumName());
                holder.setText(R.id.item_sub_title, album.getArtist().getArtistName());
            }
        };
        mInnerAdapter.setList(getArguments().getParcelableArrayList(ALBUM_LIST_KEY));
        mAdapter = new HeaderFooterViewWrapAdapter() {
            @Override
            public void convertHeaderView(CommonViewHolder holder, int position) {
                ImageLoader.load(holder.getView(R.id.big_music_preview_cover), null, artist.getArtistName());
                holder.setText(R.id.big_music_preview_text, createSpannableString());
            }

            @Override
            public void convertFooterView(CommonViewHolder holder, int position) {
            }
        };
        mAdapter.addHeaderViews(R.layout.item_big_music_preview);
        mAdapter.wrap(mInnerAdapter);
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mTitle.setText("专辑列表");
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mBackBtn.setOnClickListener(v -> goBack());
        fixNavBarHeight(mRecyclerView);
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    public SpannableString createSpannableString() {
        Artist artist = getArguments().getParcelable(ARTIST_KEY);
        int albumCount = mInnerAdapter.getItemCount();
        String title = artist.getArtistName();
        String countOfMusic = albumCount + "张专辑";
        String content = title + "\n" + countOfMusic;
        SpannableString spannableString = new SpannableString(content);
        int pointer = 0;
        spannableString.setSpan(new RelativeSizeSpan(1.6f), 0, title.length(), 0);

        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, title.length(), 0);
        pointer += title.length() + 1;

        spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), pointer, pointer + countOfMusic.length(), 0);

        return spannableString;
    }

    public static FilterAlbumListFragment getInstance(Artist artist) {
        FilterAlbumListFragment fragment = new FilterAlbumListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARTIST_KEY, artist);
        bundle.putParcelableArrayList(ALBUM_LIST_KEY, (ArrayList<Album>) artist.getAlbums());
        fragment.setArguments(bundle);
        return fragment;
    }
}
