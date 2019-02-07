package com.zspirytus.basesdk.recyclerview;

import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;

public interface ItemViewDelegate<T> {

    boolean isForViewType(T data);

    int getLayoutId();

    void convert(CommonViewHolder holder, T data);
}
