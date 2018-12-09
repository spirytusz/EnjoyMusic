package com.zspirytus.enjoymusic.cache.viewholder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.enjoymusic.R;

public class WithHeaderCardViewHolder extends RecyclerView.ViewHolder {

    private View mItemView;
    private ConstraintLayout mRandomPlayButton;

    public WithHeaderCardViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mRandomPlayButton = mItemView.findViewById(R.id.random_play_text);
    }

    public View getItemView() {
        return mItemView;
    }

    public ConstraintLayout getRandomPlayButton() {
        return mRandomPlayButton;
    }
}
