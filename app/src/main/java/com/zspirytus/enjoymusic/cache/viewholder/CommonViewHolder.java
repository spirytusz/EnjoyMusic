package com.zspirytus.enjoymusic.cache.viewholder;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.enjoymusic.cache.MyApplication;
import com.zspirytus.enjoymusic.engine.GlideApp;

import java.io.File;

public class CommonViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private SparseArray<View> mViews;

    @LayoutRes
    private int mLayoutId;

    public CommonViewHolder(View itemView, @LayoutRes int layoutId) {
        super(itemView);
        mItemView = itemView;
        mLayoutId = layoutId;

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

    public void setImageFile(@IdRes int id, File file) {
        View view = getView(id);
        if(view != null) {
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                GlideApp.with(MyApplication.getForegroundContext())
                        .load(file)
                        .into(imageView);
            }
        }
    }

    public void setText(@IdRes int id, String text) {
        View view = getView(id);
        if(view != null) {
            if(view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(text);
            }
        }
    }
}
