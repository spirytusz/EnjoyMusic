package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.entity.HomePageChildRecyclerViewItem;
import com.zspirytus.enjoymusic.view.widget.DialogBuilder;

import java.io.File;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class HomePageChildRecyclerViewAdapter extends RecyclerView.Adapter<HomePageChildRecyclerViewAdapter.HomePageChildRecyclerViewHolder> {

    private Context mContext;
    private List<HomePageChildRecyclerViewItem> mItemList;
    private OnChildRVItemClickListener mOnChildRVItemClickListener;
    private int mType;

    HomePageChildRecyclerViewAdapter(List<HomePageChildRecyclerViewItem> itemList, int type) {
        mItemList = itemList;
        mType = type;
    }

    @Override
    public HomePageChildRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_page_child_recyclerview_item, parent, false);
        return new HomePageChildRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomePageChildRecyclerViewHolder holder, int position) {
        HomePageChildRecyclerViewItem item = mItemList.get(position);
        String imgPath = item.getImgPath();
        final String title = item.getTitle();
        String subTitle = item.getSubTitle();
        if (imgPath != null) {
            GlideApp.with(mContext).load(new File(imgPath)).into(holder.mCover);
        }
        holder.mTitle.setText(title);
        holder.mSubTitle.setText(subTitle);
        holder.mVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mType) {
                    case 0:
                        final String[] musicItems = new String[]{"音乐信息", "获取专辑封面", "获取歌词"};
                        DialogBuilder.showSelectDialog(mContext, musicItems).setTitle(title).create().show();
                        break;
                    case 1:
                        final String[] AlbumItems = new String[]{"专辑信息", "获取专辑封面", "获取歌词"};
                        DialogBuilder.showSelectDialog(mContext, AlbumItems).setTitle(title).create().show();
                        break;
                    case 2:
                        final String[] artistItems = new String[]{"艺术家信息", "获取专辑封面", "获取歌词"};
                        DialogBuilder.showSelectDialog(mContext, artistItems).setTitle(title).create().show();
                        break;
                }
            }
        });
        if (mOnChildRVItemClickListener != null) {
            holder.mHomePageChildRecyclerViewHolderItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnChildRVItemClickListener.onChildRVItemClick(title, holder.getAdapterPosition(), mType);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnChildRVItemClickListener(OnChildRVItemClickListener onChildRVItemClickListener) {
        mOnChildRVItemClickListener = onChildRVItemClickListener;
    }

    static class HomePageChildRecyclerViewHolder extends RecyclerView.ViewHolder {

        private View mHomePageChildRecyclerViewHolderItemView;
        private AppCompatTextView mTitle;
        private AppCompatTextView mSubTitle;
        private AppCompatImageView mCover;
        private AppCompatImageView mVert;

        HomePageChildRecyclerViewHolder(View itemView) {
            super(itemView);
            mHomePageChildRecyclerViewHolderItemView = itemView;
            mTitle = itemView.findViewById(R.id.home_page_child_rv_title);
            mSubTitle = itemView.findViewById(R.id.home_page_child_rv_sub_title);
            mCover = itemView.findViewById(R.id.home_page_child_rv_img);
            mVert = itemView.findViewById(R.id.home_page_child_rv_vert);
        }
    }

    public interface OnChildRVItemClickListener {
        void onChildRVItemClick(String cardTitle, int position, int type);
    }
}
