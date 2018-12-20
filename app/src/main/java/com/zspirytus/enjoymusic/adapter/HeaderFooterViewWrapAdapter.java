package com.zspirytus.enjoymusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.utils.LogUtil;

public abstract class HeaderFooterViewWrapAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    private static final int HEADER_VIEW_TYPE = 2333;
    private static final int INNER_VIEW_TYPE = 666;
    private static final int FOOTER_VIEW_TYPE = 777;

    private int mHeaderViews = 1;
    private int mFooterViews = 0;

    private RecyclerView.Adapter<CommonViewHolder> mInnerAdapter;

    public HeaderFooterViewWrapAdapter(RecyclerView.Adapter<CommonViewHolder> adapter) {
        mInnerAdapter = adapter;
    }

    @Override

    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_VIEW_TYPE) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(getHeaderLayoutId(), parent, false);
            return new CommonViewHolder(headerView, getHeaderLayoutId());
        } else if (viewType == INNER_VIEW_TYPE) {
            return mInnerAdapter.onCreateViewHolder(parent, viewType);
        } else {
            View footerView = LayoutInflater.from(parent.getContext()).inflate(getFooterLayoutId(), parent, false);
            return new CommonViewHolder(footerView, getFooterLayoutId());
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
        return mInnerAdapter.getItemCount() + mHeaderViews + mFooterViews;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderViews) {
            return HEADER_VIEW_TYPE;
        } else if (position < mHeaderViews + mInnerAdapter.getItemCount()) {
            return INNER_VIEW_TYPE;
        } else {
            return FOOTER_VIEW_TYPE;
        }
    }

    public abstract int getHeaderLayoutId();

    public abstract int getFooterLayoutId();

    public abstract void convertHeaderView(CommonViewHolder holder, int position);

    public abstract void convertFooterView(CommonViewHolder holder, int position);
}
