package com.zspirytus.enjoymusic.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.LayoutManager;

public class SegmentLoadAdapter extends Adapter<CommonViewHolder> {

    private static final String TAG = "SegmentLoadAdapter";
    private static final int MAX_LOAD_SEGMENT = 10;

    private Adapter<CommonViewHolder> mInnerAdapter;
    private int mHeadIndex;

    public SegmentLoadAdapter(Adapter<CommonViewHolder> innerAdapter) {
        mInnerAdapter = innerAdapter;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mHeadIndex + MAX_LOAD_SEGMENT;
    }

    @Override
    public int getItemViewType(int position) {
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LayoutManager manager = recyclerView.getLayoutManager();
                int itemCount = getItemCount();
                int lastPosition = 0;
                if (manager instanceof GridLayoutManager) {
                    lastPosition = ((GridLayoutManager) manager).findLastVisibleItemPosition();
                } else if (manager instanceof LinearLayoutManager) {
                    lastPosition = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == itemCount - 1) {
                    loadMore();
                }
            }
        });
    }

    private void loadMore() {
        mHeadIndex += MAX_LOAD_SEGMENT;
        notifyDataSetChanged();
    }
}
