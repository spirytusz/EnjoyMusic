package com.zspirytus.enjoymusic.adapter;

import android.support.v7.widget.RecyclerView;

import com.zspirytus.enjoymusic.interfaces.annotations.BindAdapterItemLayoutId;

public abstract class BaseRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public int getItemLayoutId() {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(BindAdapterItemLayoutId.class)) {
            BindAdapterItemLayoutId bindAdapterItemLayoutId = clazz.getAnnotation(BindAdapterItemLayoutId.class);
            int layoutId = bindAdapterItemLayoutId.value();
            return layoutId;
        }
        return 0;
    }
}
