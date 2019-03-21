package com.zspirytus.basesdk.recyclerview.adapter;

import com.zspirytus.basesdk.entity.SelectableItem;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

public abstract class SingleSelectedAdapter<T> extends CommonRecyclerViewAdapter<T> {

    private List<SelectableItem<T>> mSelectableData;
    private int mSelectedPosition;

    @Override
    public List<T> getList() {
        return super.getList();
    }

    public SingleSelectedAdapter() {
        mSelectableData = new ArrayList<>();
    }

    @Override
    public void setList(List<T> list) {
        super.setList(list);
        mSelectableData.clear();
        for (T t : list) {
            SelectableItem<T> selectableItem = new SelectableItem<>(t);
            mSelectableData.add(selectableItem);
        }
    }

    @Override
    public void addData(List<T> list) {
        super.addData(list);
        for (T t : list) {
            SelectableItem<T> selectableItem = new SelectableItem<>(t);
            mSelectableData.add(selectableItem);
        }
    }

    @Override
    public void convert(CommonViewHolder holder, T t, int position) {
        convertWithSelected(holder, t, mSelectableData.get(position).isSelected(), position);
    }

    public void setSelected(int postion) {
        if (mSelectableData.size() > postion) {
            mSelectableData.get(mSelectedPosition).setSelected(false);
            mSelectableData.get(postion).setSelected(true);
            notifyItemChanged(postion);
            notifyItemChanged(mSelectedPosition);
            mSelectedPosition = postion;
        } else {
            throw new IllegalArgumentException("position must not bigger than dataList size!");
        }
    }

    public void setSelected(T t) {
        int tPos = 0;
        for (int i = 0; i < mSelectableData.size(); i++) {
            if (t.equals(mSelectableData.get(i).getT())) {
                tPos = i;
            }
        }
        setSelected(tPos);
    }

    public abstract void convertWithSelected(CommonViewHolder holder, T t, boolean isSelected, int position);
}
