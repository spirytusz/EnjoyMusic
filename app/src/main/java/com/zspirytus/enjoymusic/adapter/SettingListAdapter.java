package com.zspirytus.enjoymusic.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;

import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.listitem.SettingItem;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

public class SettingListAdapter extends MultiItemAdapter<SettingItem> {

    public SettingListAdapter() {
        addTitleDelegate();
        addAudioEffectDelegate();
        addDividerLine();
    }

    private void addTitleDelegate() {
        ItemViewDelegate<SettingItem> delegate = new ItemViewDelegate<SettingItem>() {
            @Override
            public boolean isForViewType(SettingItem data) {
                return data.isTitle();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_title;
            }

            @Override
            public void convert(CommonViewHolder holder, SettingItem data) {
                holder.setText(R.id.title, data.getTitle());
                holder.setTextColor(R.id.title, R.color.colorPrimary);
            }
        };
        addDelegate(delegate);
    }

    private void addAudioEffectDelegate() {
        ItemViewDelegate<SettingItem> delegate = new ItemViewDelegate<SettingItem>() {
            @Override
            public boolean isForViewType(SettingItem data) {
                return data.isAudioEffect();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_simple;
            }

            @Override
            public void convert(CommonViewHolder holder, SettingItem data) {
                holder.setText(R.id.item_text, data.getAudioEffectItem().getTitle());
                if (data.getAudioEffectItem().isSingleEffect()) {
                    holder.setVisibility(R.id.item_switch, View.VISIBLE);
                    Switch checked = holder.getView(R.id.item_switch);
                    checked.setChecked(data.getAudioEffectItem().isChecked());
                }
                if (mListener != null) {
                    holder.getItemView().setOnClickListener(v -> mListener.onItemClick(holder.getItemView(), holder.getAdapterPosition()));
                }
            }
        };
        addDelegate(delegate);
    }

    private void addDividerLine() {
        ItemViewDelegate<SettingItem> delegate = new ItemViewDelegate<SettingItem>() {
            @Override
            public boolean isForViewType(SettingItem data) {
                return data.isDividerLine();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_divider_line;
            }

            @Override
            public void convert(CommonViewHolder holder, SettingItem data) {
            }
        };
        addDelegate(delegate);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int position = parent.getChildAdapterPosition(view);
                if (getData().get(position).isTitle()) {
                    outRect.top = PixelsUtil.dp2px(parent.getContext(), 8);
                    outRect.left = PixelsUtil.dp2px(parent.getContext(), 28);
                }
            }
        });
    }
}
