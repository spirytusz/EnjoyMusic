package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.viewholder.HeaderViewHolder;
import com.zspirytus.enjoymusic.cache.viewholder.MusicCardViewHolder;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.annotations.BindAdapterItemLayoutId;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewHeaderClickListener;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/12/10.
 */

@BindAdapterItemLayoutId(R.layout.item_card_view_type)
public class WithHeaderCardViewItemRecyclerViewAdapter<T>
        extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final int HEADER_FLAG = 1;
    private static final int CONTENT_FLAG = 2;

    private Context mContext;
    private List<T> mList;
    private int mHeaderCount;

    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private OnRecyclerViewHeaderClickListener mOnHeaderClickListener;

    public WithHeaderCardViewItemRecyclerViewAdapter(List<T> list, int headerCount) {
        super();
        mList = list;
        mHeaderCount = headerCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType != HEADER_FLAG) {
            View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
            return new MusicCardViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_page_rv_header, parent, false);
            return new HeaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        T t = mList.get(position);
        if (t instanceof HomePageRecyclerViewItem) {
            HomePageRecyclerViewItem item = (HomePageRecyclerViewItem) t;
            if (getItemViewType(position) == CONTENT_FLAG) {
                final MusicCardViewHolder musicHolder = (MusicCardViewHolder) holder;
                Music itemMusic = item.getmMusic();
                String coverFilePath = itemMusic.getMusicThumbAlbumCoverPath();
                String musicName = itemMusic.getMusicName();
                String musicAlbum = itemMusic.getMusicAlbumName();
                if (coverFilePath != null) {
                    GlideApp.with(mContext).load(new File(coverFilePath)).into(musicHolder.getCoverImageView());
                }
                if (musicName != null) {
                    musicHolder.getTitleTextView().setText(musicName);
                }
                if (musicAlbum != null) {
                    musicHolder.getSubTitleTextView().setText(musicAlbum);
                }
                if (mOnItemClickListener != null) {
                    musicHolder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(musicHolder.getItemView(), musicHolder.getLayoutPosition());
                        }
                    });
                }
            } else {
                if (mOnHeaderClickListener != null) {
                    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                    final View mRandomPlayButton = headerViewHolder.getRandomPlayButton();
                    mRandomPlayButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnHeaderClickListener.onHeaderClick(mRandomPlayButton);
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderCount) {
            return HEADER_FLAG;
        } else {
            return CONTENT_FLAG;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnHeaderClickListener(OnRecyclerViewHeaderClickListener onHeaderClickListener) {
        mOnHeaderClickListener = onHeaderClickListener;
    }
}
