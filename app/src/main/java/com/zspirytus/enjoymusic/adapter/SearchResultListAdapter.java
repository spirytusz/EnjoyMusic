package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.listitem.SearchResult;

public class SearchResultListAdapter extends MultiItemAdapter<SearchResult> {

    public SearchResultListAdapter() {
        addTitleDelegate();
        addMusicDelegate();
        addAlbumDelegate();
        addArtistDelegate();
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
                ImageLoader.load(holder.getView(R.id.item_cover), data.getMusic().getAlbum().getAlbumArt(), data.getMusic().getMusicName());
                holder.setText(R.id.item_title, data.getMusic().getMusicName());

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
                ImageLoader.load(holder.getView(R.id.item_cover), data.getAlbum().getAlbumArt(), data.getAlbum().getAlbumName());
                holder.setText(R.id.item_title, data.getAlbum().getAlbumName());
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
                ImageLoader.load(holder.getView(R.id.item_cover), null, data.getArtist().getArtistName());
                holder.setText(R.id.item_title, data.getArtist().getArtistName());
            }
        };
        addDelegate(delegate);
    }
}
