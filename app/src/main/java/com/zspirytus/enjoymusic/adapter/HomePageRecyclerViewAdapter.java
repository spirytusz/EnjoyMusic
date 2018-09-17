package com.zspirytus.enjoymusic.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/15.
 */

public class HomePageRecyclerViewAdapter extends RecyclerView.Adapter<HomePageRecyclerViewAdapter.MyHomePageRecyclerViewHolder> {

    private Context mContext;
    private List<HomePageRecyclerViewItem> mItemList;

    public HomePageRecyclerViewAdapter(List<HomePageRecyclerViewItem> itemList) {
        mItemList = itemList;
    }

    @Override
    public MyHomePageRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_page, parent, false);
        return new MyHomePageRecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyHomePageRecyclerViewHolder holder, final int position) {
        HomePageRecyclerViewItem item = mItemList.get(position);
        holder.mMyHomePageRecyclerViewHolderTitle.setText(item.getTitle());
        switch (position) {
            case 1:
                holder.mMyHomePageRecyclerViewHolderRecyclerView.setLayoutManager(LayoutManagerFactory.createGridLayoutManager(mContext, 2));
                break;
            default:
                holder.mMyHomePageRecyclerViewHolderRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL));
        }
        holder.mMyHomePageRecyclerViewHolderRecyclerView.setAdapter(new HomePageChildRecyclerViewAdapter(item.getHomePageChildRecyclerViewItems()));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    static class MyHomePageRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView mMyHomePageRecyclerViewHolderTitle;
        private RecyclerView mMyHomePageRecyclerViewHolderRecyclerView;

        MyHomePageRecyclerViewHolder(View itemView) {
            super(itemView);
            mMyHomePageRecyclerViewHolderTitle = itemView.findViewById(R.id.item_home_page_tv);
            mMyHomePageRecyclerViewHolderRecyclerView = itemView.findViewById(R.id.item_home_page_rv);
        }
    }
}
