package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.viewholder.CommonViewHolder;
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
        extends BaseRecyclerViewAdapter<CommonViewHolder> {

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
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType != HEADER_FLAG) {
            View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
            return new CommonViewHolder(view, getItemLayoutId());
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_page_rv_header, parent, false);
            return new CommonViewHolder(view,R.layout.home_page_rv_header);
        }
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, int position) {
        T t = mList.get(position);
        if (t instanceof HomePageRecyclerViewItem) {
            HomePageRecyclerViewItem item = (HomePageRecyclerViewItem) t;
            if (getItemViewType(position) == CONTENT_FLAG) {
                Music itemMusic = item.getmMusic();
                String coverFilePath = itemMusic.getMusicThumbAlbumCoverPath();
                String musicName = itemMusic.getMusicName();
                String musicAlbum = itemMusic.getMusicAlbumName();
                if (coverFilePath != null) {
                    File coverFile = MusicCoverFileCache.getInstance().getCoverFile(coverFilePath);
                    holder.setImageFile(R.id.item_cover, coverFile);
                }
                if (musicName != null) {
                    holder.setText(R.id.item_title, musicName);
                }
                if (musicAlbum != null) {
                    holder.setText(R.id.item_sub_title, musicAlbum);
                }
                if (mOnItemClickListener != null) {
                    holder.getItemView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickListener.onItemClick(holder.getItemView(), holder.getLayoutPosition());
                        }
                    });
                }
            } else {
                if (mOnHeaderClickListener != null) {
                    final View mRandomPlayButton = holder.getView(R.id.random_play_text);
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
