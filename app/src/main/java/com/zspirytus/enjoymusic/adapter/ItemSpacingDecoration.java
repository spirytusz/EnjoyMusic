package com.zspirytus.enjoymusic.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

public class ItemSpacingDecoration extends RecyclerView.ItemDecoration {

    private int mTopPadding;
    private int mBottomPadding;
    private int mStartPadding;
    private int mEndPadding;

    private int mHeaderCount;
    private int mColumnCount;

    public ItemSpacingDecoration(int topPadding, int bottomPadding, int startPadding, int endPadding, int headerCount, int columnCount) {
        mTopPadding = PixelsUtil.dp2px(MainApplication.getForegroundContext(), topPadding);
        mBottomPadding = PixelsUtil.dp2px(MainApplication.getForegroundContext(), bottomPadding);
        mStartPadding = PixelsUtil.dp2px(MainApplication.getForegroundContext(), startPadding);
        mEndPadding = PixelsUtil.dp2px(MainApplication.getForegroundContext(), endPadding);

        mHeaderCount = headerCount;
        mColumnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int truePosition = parent.getChildLayoutPosition(view) - mHeaderCount;
        int itemCount = parent.getLayoutManager().getItemCount() - mHeaderCount;

        if (truePosition >= 0 && truePosition < mColumnCount) {
            // 第一排
            outRect.top = mTopPadding / 2;
            outRect.bottom = mBottomPadding / 2;
            outRect.left = mStartPadding / 2;
            outRect.right = mEndPadding / 2;
        } else if (truePosition >= itemCount - mColumnCount && truePosition < itemCount) {
            // 最后一排
            outRect.top = mTopPadding / 2;
            outRect.bottom = mBottomPadding / 2;
            outRect.left = mStartPadding / 2;
            outRect.right = mEndPadding / 2;
        } else {
            // 中间
            outRect.top = mTopPadding / 2;
            outRect.bottom = mBottomPadding / 2;
            outRect.left = mStartPadding / 2;
            outRect.right = mEndPadding / 2;
        }
    }
}
