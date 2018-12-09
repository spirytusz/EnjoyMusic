package com.zspirytus.enjoymusic.cache.viewholder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.enjoymusic.R;

public class MusicCardViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;

    private AppCompatImageView mCover;
    private AppCompatTextView mTitle;
    private AppCompatTextView mSubTitle;
    private AppCompatImageView mMoreInfoButton;

    public MusicCardViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mCover = mItemView.findViewById(R.id.item_cover);
        mTitle = mItemView.findViewById(R.id.item_title);
        mSubTitle = mItemView.findViewById(R.id.item_sub_title);
        mMoreInfoButton = mItemView.findViewById(R.id.item_more_info_button);
    }

    public View getItemView() {
        return mItemView;
    }

    public AppCompatImageView getCoverImageView() {
        return mCover;
    }

    public AppCompatTextView getTitleTextView() {
        return mTitle;
    }

    public AppCompatTextView getSubTitleTextView() {
        return mSubTitle;
    }

    public AppCompatImageView getMoreInfoButton() {
        return mMoreInfoButton;
    }
}
