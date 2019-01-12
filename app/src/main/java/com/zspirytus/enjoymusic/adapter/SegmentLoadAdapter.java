package com.zspirytus.enjoymusic.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zspirytus.enjoymusic.utils.LogUtil;

import static android.support.v7.widget.RecyclerView.LayoutManager;

public abstract class SegmentLoadAdapter<T> extends CommonRecyclerViewAdapter<T> {

    private static final String TAG = "SegmentLoadAdapter";
    private static final int MAX_LOAD_SEGMENT = 10;

    private int mHeadIndex;

    @Override
    public int getItemCount() {
        return mHeadIndex + MAX_LOAD_SEGMENT;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        LogUtil.e(TAG, TAG + ": onAttachedToRecyclerView");
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
                LogUtil.e(TAG, "newState == RecyclerView.SCROLL_STATE_IDLE? " + Boolean.toString(newState == RecyclerView.SCROLL_STATE_IDLE));
                LogUtil.e(TAG, "lastPosition = " + lastPosition);
                LogUtil.e(TAG, "itemCount = " + itemCount);
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
