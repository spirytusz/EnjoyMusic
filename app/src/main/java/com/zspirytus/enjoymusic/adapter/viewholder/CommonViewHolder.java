package com.zspirytus.enjoymusic.adapter.viewholder;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.MusicCoverFileCache;
import com.zspirytus.enjoymusic.engine.GlideApp;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.listeners.OnRecyclerViewItemClickListener;

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
        if (view != null && file != null) {
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                GlideApp.with(MainApplication.getForegroundContext())
                        .load(file)
                        .error(R.drawable.ic_music_note_black_48dp)
                        .into(imageView);
            }
        }
    }

    public void setImagePath(@IdRes int id, String path) {
        if (path != null) {
            File file = MusicCoverFileCache.getInstance().getCoverFile(path);
            setImageFile(id, file);
        }
    }

    public void setText(@IdRes int id, String text) {
        View view = getView(id);
        if (view != null && text != null) {
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setText(text);
            }
        }
    }

    public void setVisibility(@IdRes int id, int visibility) {
        View view = getView(id);
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public void setOnItemClickListener(final OnRecyclerViewItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(mItemView, getAdapterPosition());
            }
        });
    }

    public void setOnItemClickListener(@IdRes int viewId, View.OnClickListener listener) {
        getView(viewId).setOnClickListener(listener);
    }
}
