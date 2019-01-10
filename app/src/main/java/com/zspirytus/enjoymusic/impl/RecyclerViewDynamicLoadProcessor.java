package com.zspirytus.enjoymusic.impl;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.zspirytus.enjoymusic.adapter.CommonRecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;

public class RecyclerViewDynamicLoadProcessor<T> extends RecyclerView.OnScrollListener {

    private static final int MAX_LOAD_SEGEMENT = 10;

    private List<T> mData;
    private int mCurrentHeadIndex;

    public RecyclerViewDynamicLoadProcessor(List<T> data) {
        mData = data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        int lastPos;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            lastPos = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof LinearLayoutManager) {
            lastPos = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else {
            int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
            Arrays.sort(lastPositions);
            lastPos = lastPositions[0];
        }
        if (lastPos == recyclerView.getAdapter().getItemCount() - 1) {
            CommonRecyclerViewAdapter<T> adapter = (CommonRecyclerViewAdapter) recyclerView.getAdapter();
            adapter.addData(mData.subList(mCurrentHeadIndex, mCurrentHeadIndex + MAX_LOAD_SEGEMENT));
            mCurrentHeadIndex += MAX_LOAD_SEGEMENT;
        }
    }

}
