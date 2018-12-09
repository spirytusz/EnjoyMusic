package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.viewholder.MusicCardViewHolder;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.annotations.BindAdapterItemLayoutId;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.view.widget.DialogBuilder;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/12.
 */

@BindAdapterItemLayoutId(R.layout.item_card_view_type)
public class CardViewRecyclerViewItemRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<MusicCardViewHolder> {

    private Context mContext;
    private List<T> mList;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public CardViewRecyclerViewItemRecyclerViewAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public MusicCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
        return new MusicCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MusicCardViewHolder holder, int position) {
        T t = mList.get(position);
        String coverFilePath = "";
        String title = "";
        String subTitle = "";
        if (t instanceof Music) {
            Music music = (Music) t;
            coverFilePath = music.getMusicThumbAlbumCoverPath();
            title = music.getMusicName();
            subTitle = music.getMusicAlbumName();
        } else if (t instanceof Album) {
            Album album = (Album) t;
            coverFilePath = album.getAlbumCoverPath();
            title = album.getAlbumName();
            subTitle = album.getArtist();
        }
        if (coverFilePath.length() > 0) {
            GlideApp.with(mContext).load(new File(coverFilePath)).into(holder.getCoverImageView());
        }
        holder.getTitleTextView().setText(title);
        holder.getSubTitleTextView().setText(subTitle);
        holder.getMoreInfoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[]{"专辑信息", "获取专辑封面", "获取歌词"};
                DialogBuilder.showSelectDialog(mContext, items).create().show();
            }
        });
        bindListener(holder);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setList(List<T> list) {
        mList = list;
    }

    public void bindListener(final MusicCardViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.getItemView(), holder.getLayoutPosition());
                }
            });
        }
    }
}
