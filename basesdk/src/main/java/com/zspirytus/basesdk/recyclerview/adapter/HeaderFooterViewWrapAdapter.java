package com.zspirytus.basesdk.recyclerview.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;

public abstract class HeaderFooterViewWrapAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    private static final int HEADER_VIEW_TYPE = 2333;
    private static final int INNER_VIEW_TYPE = 666;
    private static final int FOOTER_VIEW_TYPE = 777;

    private SparseArrayCompat<Integer> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<Integer> mFooterViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter<CommonViewHolder> mInnerAdapter;

    public HeaderFooterViewWrapAdapter() {
    }

    public HeaderFooterViewWrapAdapter(RecyclerView.Adapter<CommonViewHolder> adapter) {
        mInnerAdapter = adapter;
    }

    @Override

    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            int headerLayoutId = mHeaderViews.get(HEADER_VIEW_TYPE);
            View headerView = LayoutInflater.from(parent.getContext()).inflate(headerLayoutId, parent, false);
            return new CommonViewHolder(headerView);
        } else if (viewType == FOOTER_VIEW_TYPE) {
            int footerViewLayoutId = mFooterViews.get(FOOTER_VIEW_TYPE);
            View footerView = LayoutInflater.from(parent.getContext()).inflate(footerViewLayoutId, parent, false);
            return new CommonViewHolder(footerView);
        } else {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == HEADER_VIEW_TYPE) {
            convertHeaderView(holder, position);
        } else if (viewType == INNER_VIEW_TYPE) {
            mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
        } else {
            convertFooterView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter != null ? mInnerAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderViews.size()) {
            return HEADER_VIEW_TYPE;
        } else if (position < mHeaderViews.size() + mInnerAdapter.getItemCount()) {
            return INNER_VIEW_TYPE;
        } else {
            return FOOTER_VIEW_TYPE;
        }
    }

    public void addHeaderViews(@LayoutRes int layoutId) {
        mHeaderViews.put(HEADER_VIEW_TYPE, layoutId);
    }

    public void addFooterViews(@LayoutRes int layoutId) {
        mFooterViews.put(FOOTER_VIEW_TYPE, layoutId);
    }

    public int getHeaderViewCount() {
        return mHeaderViews.size();
    }

    public int getFooterViewCount() {
        return mFooterViews.size();
    }

    public abstract void convertHeaderView(CommonViewHolder holder, int position);

    public abstract void convertFooterView(CommonViewHolder holder, int position);
}
