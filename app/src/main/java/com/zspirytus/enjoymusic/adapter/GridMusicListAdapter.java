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
import com.zspirytus.enjoymusic.entity.Album;

import java.io.File;
import java.util.List;

/**
 * Created by zhangkunwei on 2018/9/12.
 */

public class GridMusicListAdapter extends RecyclerView.Adapter<GridMusicListAdapter.MyHolder> {

    private Context mContext;
    private List<Album> mAlbumList;
    private OnItemClickListener mOnItemClickListener;

    public GridMusicListAdapter() {
        super();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setAlbumList(List<Album> albumList) {
        mAlbumList = albumList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_album_music_list, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        Album album = mAlbumList.get(position);
        String albumCoverPath = album.getAlbumCoverPath();
        if (albumCoverPath != null && albumCoverPath.length() > 0) {
            Glide.with(mContext).load(new File(albumCoverPath)).into(holder.mHolderAlbumCover);
        }
        holder.mHolderAlbumName.setText(album.getAlbumName());
        holder.mHolderAlbumArtist.setText(album.getArtist());
        if (mOnItemClickListener != null) {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.mItemView, holder.getLayoutPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        private View mItemView;

        private AppCompatImageView mHolderAlbumCover;
        private AppCompatTextView mHolderAlbumName;
        private AppCompatTextView mHolderAlbumArtist;
        private AppCompatImageView mHolderAlbumVert;

        MyHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mHolderAlbumCover = itemView.findViewById(R.id.album_cover);
            mHolderAlbumName = itemView.findViewById(R.id.album_name);
            mHolderAlbumArtist = itemView.findViewById(R.id.album_artist_name);
            mHolderAlbumVert = itemView.findViewById(R.id.album_artist_vert);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
