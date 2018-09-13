package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.view.widget.DialogBuilder;
import com.zspirytus.enjoymusic.view.widget.RoundCornerImageView;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/8/3.
 */

public class LinearMusicListAdapter extends RecyclerView.Adapter<LinearMusicListAdapter.MyViewHolder> {

    private Context mContext;
    private List<Music> mAllMusicItemList;
    private List<Artist> mArtistMusicItemList;
    private OnItemClickListener onItemClickListener;
    private int mItemType;

    public LinearMusicListAdapter(int itemType) {
        mItemType = itemType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_all_music_list, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mItemType == Constant.RecyclerViewItemType.ALL_MUSIC_ITEM_TYPE && mAllMusicItemList != null) {
            bindAllMusicListItem(holder, position);
        } else if (mItemType == Constant.RecyclerViewItemType.ARTIST_MUSIC_ITEM_TYPE && mArtistMusicItemList != null) {
            bindArtistMusicListItem(holder, position);
        }
        bindListener(holder, position);
    }

    @Override
    public int getItemCount() {
        if (mAllMusicItemList != null) {
            return mAllMusicItemList.size();
        }
        return mArtistMusicItemList.size();
    }

    public void setAllMusicItemList(List<Music> list) {
        mAllMusicItemList = list;
    }

    public void setArtistMusicItemList(List<Artist> list) {
        mArtistMusicItemList = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void bindAllMusicListItem(final MyViewHolder holder, final int position) {
        final Music music = mAllMusicItemList.get(position);
        if (music.getMusicThumbAlbumCoverPath() != null) {
            Glide.with(mContext).load(new File(music.getMusicThumbAlbumCoverPath()))
                    .into(holder.mCover);
        } else {
            holder.mCover.setImageResource(R.color.grey);
        }
        holder.mMusicName.setText(music.getMusicName());
        holder.mMusicArtist.setText(music.getMusicArtist());
        holder.mMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = DialogBuilder.showSelectDialog(mContext);
                String title = music.getMusicName();
                builder.setTitle(title).create().show();
            }
        });
    }

    private void bindArtistMusicListItem(final MyViewHolder holder, final int position) {
        final Artist artist = mArtistMusicItemList.get(position);
        holder.mMusicName.setText(artist.getArtistName());
        holder.mMusicArtist.setText(artist.getMusicCount() + " 首歌曲");
        holder.mMoreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = DialogBuilder.showSelectDialog(mContext);
                String title = artist.getArtistName();
                builder.setTitle(title).create().show();
            }
        });
    }

    private void bindListener(final MyViewHolder holder, final int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position);
                }
            });
        }
    }

    protected static class MyViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        private AppCompatImageView mTag;
        private RoundCornerImageView mCover;
        private AppCompatTextView mMusicName;
        private AppCompatTextView mMusicArtist;
        private AppCompatImageView mMoreVert;

        private MyViewHolder(View itemView) {
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
