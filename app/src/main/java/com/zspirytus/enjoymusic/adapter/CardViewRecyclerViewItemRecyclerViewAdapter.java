package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.ForegroundMusicCache;
import com.zspirytus.enjoymusic.cache.viewholder.MusicCardViewHolder;
import com.zspirytus.enjoymusic.cache.viewholder.WithHeaderCardViewHolder;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
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
public class CardViewRecyclerViewItemRecyclerViewAdapter<T>
        extends BaseRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private static final int NO_MULTI_VIEW_HOLDER = -1;

    private Context mContext;
    private List<T> mList;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public CardViewRecyclerViewItemRecyclerViewAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == NO_MULTI_VIEW_HOLDER || viewType == HomePageRecyclerViewItem.ITEM_TYPE_CONTENT) {
            View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
            return new MusicCardViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.home_page_rv_header, parent, false);
            return new WithHeaderCardViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        T t = mList.get(position);
        if (!(t instanceof HomePageRecyclerViewItem)) {
            MusicCardViewHolder musicCardViewHolder = (MusicCardViewHolder) holder;
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
                GlideApp.with(mContext).load(new File(coverFilePath)).into(musicCardViewHolder.getCoverImageView());
            }
            musicCardViewHolder.getTitleTextView().setText(title);
            musicCardViewHolder.getSubTitleTextView().setText(subTitle);
            musicCardViewHolder.getMoreInfoButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] items = new String[]{"专辑信息", "获取专辑封面", "获取歌词"};
                    DialogBuilder.showSelectDialog(mContext, items).create().show();
                }
            });
            bindListener(musicCardViewHolder);
        } else if (((HomePageRecyclerViewItem) t).getmItemType() != HomePageRecyclerViewItem.ITEM_TYPE_HEADER) {
            Music music = ((HomePageRecyclerViewItem) t).getmMusic();
            MusicCardViewHolder musicCardViewHolder = (MusicCardViewHolder) holder;
            if (music.getMusicThumbAlbumCoverPath() != null) {
                GlideApp.with(mContext).load(new File(music.getMusicThumbAlbumCoverPath())).into(musicCardViewHolder.getCoverImageView());
            }
            musicCardViewHolder.getTitleTextView().setText(music.getMusicName());
            musicCardViewHolder.getSubTitleTextView().setText(music.getMusicAlbumName());
            musicCardViewHolder.getMoreInfoButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String[] items = new String[]{"专辑信息", "获取专辑封面", "获取歌词"};
                    DialogBuilder.showSelectDialog(mContext, items).create().show();
                }
            });
            bindListener(musicCardViewHolder);
        } else {
            ((WithHeaderCardViewHolder) holder).getRandomPlayButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ForegroundMusicController.getInstance().play(ForegroundMusicCache.getInstance().getAllMusicList().get(0));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) instanceof HomePageRecyclerViewItem) {
            return ((HomePageRecyclerViewItem) mList.get(position)).getmItemType();
        }
        return NO_MULTI_VIEW_HOLDER;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public void setList(List<T> list) {
        mList = list;
    }

    private void bindListener(final MusicCardViewHolder holder) {
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
