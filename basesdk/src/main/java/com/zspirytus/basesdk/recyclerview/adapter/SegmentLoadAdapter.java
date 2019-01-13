package com.zspirytus.basesdk.recyclerview.adapter;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;

import static android.support.v7.widget.RecyclerView.Adapter;
import static android.support.v7.widget.RecyclerView.LayoutManager;

public class SegmentLoadAdapter extends Adapter<CommonViewHolder> {

    private static final String TAG = "SegmentLoadAdapter";
    private static final int MAX_LOAD_SEGMENT = 20;

    private Adapter<CommonViewHolder> mInnerAdapter;
    private int mSegmentSize;
    private int mHeadIndex;
    private int mHeaderViewCount;

    public SegmentLoadAdapter(Adapter<CommonViewHolder> innerAdapter) {
        this(innerAdapter, MAX_LOAD_SEGMENT);
    }

    public SegmentLoadAdapter(Adapter<CommonViewHolder> innerAdapter, int segmentSize) {
        mInnerAdapter = innerAdapter;
        mSegmentSize = segmentSize;
        if (innerAdapter instanceof HeaderFooterViewWrapAdapter) {
            mHeaderViewCount = ((HeaderFooterViewWrapAdapter) innerAdapter).getHeaderViewCount();
        }
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
        return mHeadIndex + mHeaderViewCount + mSegmentSize <= mInnerAdapter.getItemCount() ?
                mHeadIndex + mHeaderViewCount + mSegmentSize : mInnerAdapter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ViewParent view = recyclerView.getParent();
        if (view instanceof NestedScrollView) {
            NestedScrollView nestedScrollView = (NestedScrollView) view;
            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        loadMore();
                    }
                }
            });
        } else {
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
        notifyItemRangeChanged(0, mSegmentSize + mHeaderViewCount);
    }

    private void loadMore() {
        mHeadIndex += mSegmentSize;
        notifyItemRangeChanged(mHeadIndex, MAX_LOAD_SEGMENT);
    }
}
