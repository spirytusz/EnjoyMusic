package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.view.widget.DialogBuilder;
import com.zspirytus.enjoymusic.view.widget.RoundCornerImageView;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/8/3.
 */

public class MusicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Music> mItemList;
    private OnItemClickListener onItemClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_music_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindNormalItem((MyViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setItemList(List<Music> list) {
        mItemList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void bindNormalItem(final MyViewHolder holder, int position) {
        Music item = mItemList.get(position);
        if (item.getMusicThumbAlbumCoverPath() != null) {
            Glide.with(mContext).load(new File(item.getMusicThumbAlbumCoverPath()))
                    .into(holder.mCover);
        }
        holder.mMusicName.setText(item.getMusicName());
        holder.mMusicArtist.setText(item.getMusicArtist());
        holder.mMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.showSelectDialog(mContext);
            }
        });
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, holder.getLayoutPosition());
                }
            });
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private AppCompatImageView mTag;
        private RoundCornerImageView mCover;
        private AppCompatTextView mMusicName;
        private AppCompatTextView mMusicArtist;
        private AppCompatImageView mMoreVert;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mTag = itemView.findViewById(R.id.tag);
            mCover = itemView.findViewById(R.id.cover);
            mMusicName = itemView.findViewById(R.id.music_name);
            mMusicArtist = itemView.findViewById(R.id.music_artist);
            mMoreVert = itemView.findViewById(R.id.more_vert);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
