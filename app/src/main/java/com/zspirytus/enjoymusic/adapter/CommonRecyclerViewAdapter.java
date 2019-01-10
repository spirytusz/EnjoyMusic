package com.zspirytus.enjoymusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.adapter.viewholder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private List<T> mList = new ArrayList<>();

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false);
        return new CommonViewHolder(view, getLayoutId());
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        convert(holder, mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<T> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void addData(List<T> list) {
        if (mList != null) {
            mList.addAll(list);
        } else {
            mList = list;
        }
    }

    public List<T> getList() {
        return mList;
    }

    public abstract int getLayoutId();

    public abstract void convert(CommonViewHolder holder, T t, int position);
}
