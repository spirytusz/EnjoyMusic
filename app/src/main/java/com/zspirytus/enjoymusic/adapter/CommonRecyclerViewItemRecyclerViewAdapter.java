package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.viewholder.MusicCommonViewHolder;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.interfaces.annotations.BindAdapterItemLayoutId;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;
import com.zspirytus.enjoymusic.view.widget.DialogBuilder;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/8/3.
 */

@BindAdapterItemLayoutId(R.layout.item_common_view_type)
public class CommonRecyclerViewItemRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<MusicCommonViewHolder> {

    private Context mContext;
    private List<T> mList;
    private OnRecyclerViewItemClickListener onItemClickListener;

    public CommonRecyclerViewItemRecyclerViewAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public MusicCommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
        return new MusicCommonViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MusicCommonViewHolder holder, int position) {
        T t = mList.get(position);
        String coverFilePath = "";
        String title = "";
        String subTitle = "";
        final String[] moreInfoItem;
        if (t instanceof Music) {
            Music music = (Music) t;
            coverFilePath = music.getMusicThumbAlbumCoverPath();
            title = music.getMusicName();
            subTitle = music.getMusicAlbumName();
            moreInfoItem = new String[]{"音乐信息", "获取专辑封面", "获取歌词"};
        } else if (t instanceof Album) {
            Album album = (Album) t;
            coverFilePath = album.getAlbumCoverPath();
            title = album.getAlbumName();
            subTitle = album.getArtist();
            moreInfoItem = new String[]{"专辑信息", "获取专辑封面", "获取歌词"};
        } else {
            Artist artist = (Artist) t;
            title = artist.getArtistName();
            subTitle = artist.getMusicCount() + " 首歌曲";
            moreInfoItem = new String[]{"艺术家信息", "获取艺术家封面", "获取歌词"};
        }
        if (coverFilePath.length() > 0) {
            GlideApp.with(mContext).load(new File(coverFilePath)).into(holder.getCoverImageView());
        }
        holder.getTitleTextView().setText(title);
        holder.getSubTitleTextView().setText(subTitle);
        holder.getMoreInfoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBuilder.showSelectDialog(mContext, moreInfoItem).create();
            }
        });
        bindListener(holder);
    }

    @Override
    public int getItemCount() {
        if (mList != null)
            return mList.size();
        return 0;
    }

    public void setList(List<T> list) {
        mList = list;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void bindListener(final MusicCommonViewHolder holder) {
        if (onItemClickListener != null) {
            holder.getItemView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, holder.getLayoutPosition());
                }
            });
        }
    }
}
