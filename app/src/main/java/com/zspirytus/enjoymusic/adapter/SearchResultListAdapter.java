package com.zspirytus.enjoymusic.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.basesdk.utils.PixelsUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.listitem.SearchResult;

public class SearchResultListAdapter extends MultiItemAdapter<SearchResult> {

    public SearchResultListAdapter() {
        addTitleDelegate();
        addMusicDelegate();
        addAlbumDelegate();
        addArtistDelegate();
        addDividerLineDelegate();
    }

    private void addTitleDelegate() {
        ItemViewDelegate<SearchResult> delegate = new ItemViewDelegate<SearchResult>() {
            @Override
            public boolean isForViewType(SearchResult data) {
                return data.isTitle();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_title;
            }

            @Override
            public void convert(CommonViewHolder holder, SearchResult data) {
                holder.setText(R.id.title, data.getTitle());
            }
        };
        addDelegate(delegate);
    }

    private void addMusicDelegate() {
        ItemViewDelegate<SearchResult> delegate = new ItemViewDelegate<SearchResult>() {
            @Override
            public boolean isForViewType(SearchResult data) {
                return data.isMusic();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, SearchResult data) {
                Album album = QueryExecutor.findAlbum(data.getMusic());
                ImageLoader.load(holder.getView(R.id.item_cover), album.getArtPath(), data.getMusic().getMusicName());
                holder.setText(R.id.item_title, data.getMusic().getMusicName());
                holder.setText(R.id.item_sub_title, album.getAlbumName());
                holder.setVisibility(R.id.item_more_info_button, View.GONE);
                holder.setOnItemClickListener(mListener);
            }
        };
        addDelegate(delegate);
    }

    private void addAlbumDelegate() {
        ItemViewDelegate<SearchResult> delegate = new ItemViewDelegate<SearchResult>() {
            @Override
            public boolean isForViewType(SearchResult data) {
                return data.isAlbum();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, SearchResult data) {
                Artist artist = QueryExecutor.findArtist(data.getAlbum());
                ImageLoader.load(holder.getView(R.id.item_cover), data.getAlbum().getArtPath(), data.getAlbum().getAlbumName());
                holder.setText(R.id.item_title, data.getAlbum().getAlbumName());
                holder.setText(R.id.item_sub_title, artist.getArtistName());
                holder.setVisibility(R.id.item_more_info_button, View.GONE);
                holder.setOnItemClickListener(mListener);
            }
        };
        addDelegate(delegate);
    }

    private void addArtistDelegate() {
        ItemViewDelegate<SearchResult> delegate = new ItemViewDelegate<SearchResult>() {
            @Override
            public boolean isForViewType(SearchResult data) {
                return data.isArtist();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_common_view_type;
            }

            @Override
            public void convert(CommonViewHolder holder, SearchResult data) {
                ArtistArt artistArt = QueryExecutor.findArtistArt(data.getArtist());
                String path = artistArt != null ? artistArt.getArtistArt() : null;
                ImageLoader.load(holder.getView(R.id.item_cover), path, data.getArtist().getArtistName());
                holder.setText(R.id.item_title, data.getArtist().getArtistName());
                holder.setText(R.id.item_sub_title, data.getArtist().getMumberOfTracks() + "首歌曲");
                holder.setVisibility(R.id.item_more_info_button, View.GONE);
                holder.setOnItemClickListener(mListener);
            }
        };
        addDelegate(delegate);
    }

    private void addDividerLineDelegate() {
        addDelegate(new ItemViewDelegate<SearchResult>() {
            @Override
            public boolean isForViewType(SearchResult data) {
                return data.isDividerLine();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_divider_line;
            }

            @Override
            public void convert(CommonViewHolder holder, SearchResult data) {
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (getData().get(position).isTitle()) {
                    outRect.left = PixelsUtil.dp2px(parent.getContext(), 10);
                }
            }
        });
    }
}
