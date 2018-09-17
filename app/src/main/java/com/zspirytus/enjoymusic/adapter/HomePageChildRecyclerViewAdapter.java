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
import com.zspirytus.enjoymusic.entity.HomePageChildRecyclerViewItem;

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
            Glide.with(mContext).load(new File(imgPath)).into(holder.mCover);
        }
        holder.mTitle.setText(title);
        holder.mSubTitle.setText(subTitle);
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

        HomePageChildRecyclerViewHolder(View itemView) {
            super(itemView);
            mHomePageChildRecyclerViewHolderItemView = itemView;
            mTitle = itemView.findViewById(R.id.home_page_child_rv_title);
            mSubTitle = itemView.findViewById(R.id.home_page_child_rv_sub_title);
            mCover = itemView.findViewById(R.id.home_page_child_rv_img);
        }
    }

    public interface OnChildRVItemClickListener {
        void onChildRVItemClick(String cardTitle, int position, int type);
    }
}
