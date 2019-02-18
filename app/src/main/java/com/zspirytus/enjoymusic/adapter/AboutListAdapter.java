package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.AboutItem;

public class AboutListAdapter extends MultiItemAdapter<AboutItem> {

    private OnItemClickListener mListener;

    public AboutListAdapter() {
        addTitleItemDelegate();
        addItemDelegate();
        addOnlyMainTitleItemDelegate();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private void addTitleItemDelegate() {
        ItemViewDelegate<AboutItem> delegate = new ItemViewDelegate<AboutItem>() {
            @Override
            public boolean isForViewType(AboutItem data) {
                return data.isTitle();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_about_title;
            }

            @Override
            public void convert(CommonViewHolder holder, AboutItem data) {
                holder.setText(R.id.title, data.getTitle());
            }
        };
        addDelegate(delegate);
    }

    private void addItemDelegate() {
        ItemViewDelegate<AboutItem> delegate = new ItemViewDelegate<AboutItem>() {
            @Override
            public boolean isForViewType(AboutItem data) {
                return data.isCommonItem() && !data.isOnlyMainTitle();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_about;
            }

            @Override
            public void convert(CommonViewHolder holder, AboutItem data) {
                holder.setText(R.id.item_main_title, data.getMainTitle());
                holder.setText(R.id.item_sub_title, data.getSubTitle());
                holder.setOnItemClickListener(mListener);
            }
        };
        addDelegate(delegate);
    }

    private void addOnlyMainTitleItemDelegate() {
        ItemViewDelegate<AboutItem> delegate = new ItemViewDelegate<AboutItem>() {
            @Override
            public boolean isForViewType(AboutItem data) {
                return data.isCommonItem() && data.isOnlyMainTitle();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_about_only_main_title;
            }

            @Override
            public void convert(CommonViewHolder holder, AboutItem data) {
                holder.setText(R.id.item_main_title, data.getMainTitle());
                if (mListener != null) {
                    holder.setOnItemClickListener(mListener);
                }
            }
        };
        addDelegate(delegate);
    }
}
