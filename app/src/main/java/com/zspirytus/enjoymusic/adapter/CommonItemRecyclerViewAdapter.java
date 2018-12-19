package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.cache.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
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
public class CommonItemRecyclerViewAdapter<T> extends BaseRecyclerViewAdapter<CommonViewHolder> {

    private Context mContext;
    private List<T> mList;
    private OnRecyclerViewItemClickListener onItemClickListener;

    public CommonItemRecyclerViewAdapter(List<T> list) {
        mList = list;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(getItemLayoutId(), parent, false);
        return new CommonViewHolder(view, getItemLayoutId());
    }


    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        T t = mList.get(position);
        String coverFilePath;
        String title;
        String subTitle;
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
        } else if (t instanceof Artist) {
            Artist artist = (Artist) t;
            coverFilePath = "";
            title = artist.getArtistName();
            subTitle = artist.getNumberOfTracks() + " 首歌曲";
            moreInfoItem = new String[]{"艺术家信息", "获取艺术家封面", "获取歌词"};
        } else {
            FolderSortedMusic folderSortedMusic = (FolderSortedMusic) t;
            Music firstMusicInFolder = folderSortedMusic.getFolderMusicList().get(0);
            coverFilePath = firstMusicInFolder.getMusicThumbAlbumCoverPath();
            title = folderSortedMusic.getFolderName();
            subTitle = folderSortedMusic.getParentFolderDir();
            moreInfoItem = new String[]{};
        }
        if (coverFilePath != null && coverFilePath.length() > 0) {
            File coverFile = MusicCoverFileCache.getInstance().getCoverFile(coverFilePath);
            holder.setImageFile(R.id.item_cover, coverFile);
        }
        holder.setText(R.id.item_title, title);
        holder.setText(R.id.item_sub_title, subTitle);
        holder.getView(R.id.item_more_info_button).setOnClickListener(new View.OnClickListener() {
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

    private void bindListener(final CommonViewHolder holder) {
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
