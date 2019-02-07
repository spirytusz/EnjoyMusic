package com.zspirytus.basesdk.recyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.basesdk.ItemViewDelegateManager;
import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MultiItemAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {

    private List<T> mData;
    private ItemViewDelegateManager<T> mItemViewDelegateManager;

    public MultiItemAdapter(List<T> data) {
        mData = data;
        mItemViewDelegateManager = new ItemViewDelegateManager<>();
    }

    public MultiItemAdapter() {
        this(new ArrayList<T>());
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = mItemViewDelegateManager.getLayoutIdByViewType(viewType);
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CommonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        ItemViewDelegate<T> itemViewDelegate = mItemViewDelegateManager.getItemDelegate(mData.get(position));
        itemViewDelegate.convert(holder, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemViewDelegateManager.getItemViewType(mData.get(position));
    }

    public void addDelegate(ItemViewDelegate<T> delegate) {
        mItemViewDelegateManager.addDelegate(delegate);
    }

    public void removeDelegate(ItemViewDelegate<T> delegate) {
        mItemViewDelegateManager.removeDelegate(delegate);
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public void addData(List<T> data) {
        mData.addAll(data);
    }

    public List<T> getData() {
        return mData;
    }
}
