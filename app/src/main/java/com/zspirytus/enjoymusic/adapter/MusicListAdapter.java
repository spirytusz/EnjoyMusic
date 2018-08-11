package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.model.Music;
import com.zspirytus.enjoymusic.view.customview.RoundCornerImageView;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/8/3.
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MusicListAdapter";

    private static final int NORMAL_ITEM = 1;
    private static final int BOTTOM_FULFILL = 2;
    private static final int HEADER_FULFILL = 4;

    private Context mContext;
    private List<Music> mItemList;
    private OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == NORMAL_ITEM) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.music_list_item, parent, false);
            return new MyViewHolder(view);
        } else if (viewType == BOTTOM_FULFILL) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.bottom_fulfill, parent, false);
            return new BottomFulfill(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.header_fulfill, parent, false);
            return new HeaderFulfill(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == NORMAL_ITEM) {
            bindNormalItem((MyViewHolder) holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            // if it is the first item (let the first item be null in setItemList())
            return HEADER_FULFILL;
        } else if (position < getItemCount() - 1 && position > 0) {
            // else if it is not the last item (let the last item be null in setItemList())
            return NORMAL_ITEM;
        } else {
            // else, it is the last item
            return BOTTOM_FULFILL;
        }
    }

    public void setItemList(List<Music> list) {
        list.add(null);
        list.add(0, null);
        mItemList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void bindNormalItem(final MyViewHolder holder, int position) {
        Music item = mItemList.get(position);
        if (item.getmMusicThumbAlbumUri() != null) {
            Glide.with(mContext).load(new File(item.getmMusicThumbAlbumUri()))
                    .into(holder.mCover);
        }
        holder.mMusicName.setText(item.getmMusicName());
        holder.mMusicArtist.setText(item.getmMusicArtist());
        holder.mMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(view, holder.getLayoutPosition());
                }
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private RoundCornerImageView mCover;
        private TextView mMusicName;
        private TextView mMusicArtist;
        private ImageView mMoreVert;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mCover = itemView.findViewById(R.id.cover);
            mMusicName = itemView.findViewById(R.id.music_name);
            mMusicArtist = itemView.findViewById(R.id.music_artist);
            mMoreVert = itemView.findViewById(R.id.more_vert);
        }

    }

    static class BottomFulfill extends RecyclerView.ViewHolder {

        public BottomFulfill(View itemView) {
            super(itemView);
        }
    }

    static class HeaderFulfill extends RecyclerView.ViewHolder {

        public HeaderFulfill(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
