package com.zspirytus.enjoymusic.adapter;

import android.support.annotation.LayoutRes;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.utils.LogUtil;

public abstract class HeaderFooterViewWrapAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    private static final int HEADER_VIEW_TYPE = 2333;
    private static final int INNER_VIEW_TYPE = 666;
    private static final int FOOTER_VIEW_TYPE = 777;

    private SparseArrayCompat<Integer> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<Integer> mFooterViews = new SparseArrayCompat<>();

    private RecyclerView.Adapter<CommonViewHolder> mInnerAdapter;

    public HeaderFooterViewWrapAdapter(RecyclerView.Adapter<CommonViewHolder> adapter) {
        mInnerAdapter = adapter;
    }

    @Override

    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            int headerLayoutId = mHeaderViews.get(HEADER_VIEW_TYPE);
            View headerView = LayoutInflater.from(parent.getContext()).inflate(headerLayoutId, parent, false);
            return new CommonViewHolder(headerView, headerLayoutId);
        } else if (viewType == INNER_VIEW_TYPE) {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        } else {
            int footerViewLayoutId = mFooterViews.get(FOOTER_VIEW_TYPE);
            View footerView = LayoutInflater.from(parent.getContext()).inflate(footerViewLayoutId, parent, false);
            return new CommonViewHolder(footerView, footerViewLayoutId);
        }
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        LogUtil.e(this.getClass().getSimpleName(), "position = " + position + "\tviewType = " + viewType);
        if (viewType == HEADER_VIEW_TYPE) {
            convertHeaderView(holder, position);
        } else if (viewType == INNER_VIEW_TYPE) {
            mInnerAdapter.onBindViewHolder(holder, position);
        } else {
            convertFooterView(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
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

    public abstract void convertHeaderView(CommonViewHolder holder, int position);

    public abstract void convertFooterView(CommonViewHolder holder, int position);
}
