package com.zspirytus.basesdk.recyclerview.viewholder;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;

@SuppressWarnings("unchecked")
public class CommonViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;
    private View mItemView;
    private SparseArray<View> mViews;

    public CommonViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        mItemView = view;
        mViews = new SparseArray<>();
    }

    /**
     * get ItemView
     *
     * @return
     */
    public View getItemView() {
        return mItemView;
    }

    /**
     * get Context
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * get View Cache
     *
     * @return
     */
    public SparseArray<View> getViews() {
        return mViews;
    }

    /**
     * findViewById
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getView(@IdRes int id) {
        View view = mViews.get(id);
        if (view == null) {
            view = mItemView.findViewById(id);
            mViews.put(id, view);
        }
        return (T) view;
    }

    public void setText(@IdRes int id, String text) {
        TextView textView = getView(id);
        textView.setText(text);
    }

    public void setText(@IdRes int id, Spanned text) {
        TextView textView = getView(id);
        textView.setText(text);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        if (listener != null) {
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mItemView, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener listener) {
        if (listener != null) {
            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(mItemView, getAdapterPosition());
                    return true;
                }
            });
        }
    }

    public void setImageResource(@IdRes int id, @DrawableRes int resId) {
        ImageView imageView = getView(id);
        imageView.setImageResource(resId);
    }

    public void setOnItemClickListener(@IdRes int id, View.OnClickListener listener) {
        getView(id).setOnClickListener(listener);
    }

    public void setOnItemLongClickListener(@IdRes int id, View.OnLongClickListener listener) {
        getView(id).setOnLongClickListener(listener);
    }

    public void setVisibility(@IdRes int id, int visibility) {
        getView(id).setVisibility(visibility);
    }
}
