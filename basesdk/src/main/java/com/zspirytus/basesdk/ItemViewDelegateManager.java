package com.zspirytus.basesdk;

import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;

import java.util.LinkedList;

public class ItemViewDelegateManager<T> {

    private LinkedList<ItemViewDelegate<T>> mDelegates;

    public ItemViewDelegateManager() {
        mDelegates = new LinkedList<>();
    }

    public void addDelegate(ItemViewDelegate<T> delegate) {
        if (!mDelegates.contains(delegate)) {
            mDelegates.add(delegate);
        }
    }

    public void removeDelegate(ItemViewDelegate<T> delegate) {
        mDelegates.remove(delegate);
    }

    public int getLayoutIdByViewType(int viewType) {
        return mDelegates.get(viewType).getLayoutId();
    }

    public ItemViewDelegate<T> getItemDelegate(T data) {
        for (int i = 0; i < mDelegates.size(); i++) {
            if (mDelegates.get(i).isForViewType(data)) {
                return mDelegates.get(i);
            }
        }
        return null;
    }

    public int getItemViewType(T data) {
        for (int i = 0; i < mDelegates.size(); i++) {
            if (mDelegates.get(i).isForViewType(data)) {
                return i;
            }
        }
        return -1;
    }
}
