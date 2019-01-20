package com.zspirytus.basesdk.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemSpacingDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "ItemSpacingDecoration";

    private int mTopPadding;
    private int mBottomPadding;
    private int mStartPadding;
    private int mEndPadding;

    private int mHeaderViewCount;
    private int mFooterViewCount;
    private int mMarginTop;

    private ItemSpacingDecoration(int topPadding, int bottomPadding, int startPadding, int endPadding, int headerViewCount, int footerViewCount, int marginTop) {
        mTopPadding = topPadding;
        mBottomPadding = bottomPadding;
        mStartPadding = startPadding;
        mEndPadding = endPadding;

        mHeaderViewCount = headerViewCount;
        mFooterViewCount = footerViewCount;
        mMarginTop = marginTop;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int span = 1;
        int position = parent.getChildAdapterPosition(view);
        int totalSize = parent.getAdapter().getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            span = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        if (position < mHeaderViewCount) {
            // headerView
            if (position == 0) {
                outRect.top = mMarginTop;
            }
        } else if (position < totalSize - mFooterViewCount) {
            // commonView
            // 计算上下边距
            int truePosition = position - mHeaderViewCount;
            if (truePosition < span) {
                // 第一排
                outRect.top = mTopPadding + mHeaderViewCount == 0 ? mMarginTop : 0;
                outRect.bottom = mBottomPadding / 2;
            } else if (truePosition > totalSize - mFooterViewCount - span) {
                // 最后一排
                outRect.top = mTopPadding / 2;
                outRect.bottom = mBottomPadding;
            } else {
                // 中间
                outRect.top = mTopPadding / 2;
                outRect.bottom = mBottomPadding / 2;
            }
            //计算左右边距
            if (truePosition % span == 0) {
                // 第一列
                outRect.left = mStartPadding;
                outRect.right = mEndPadding / 2;
            } else if (truePosition % span == span - 1) {
                // 最后一列
                outRect.left = mStartPadding / 2;
                outRect.right = mEndPadding;
            } else {
                // 中间
                outRect.left = mStartPadding / 2;
                outRect.right = mEndPadding / 2;
            }
        } else {
            // footerView
        }

    }

    public static class Builder {
        private int mTopPadding;
        private int mBottomPadding;
        private int mStartPadding;
        private int mEndPadding;

        private int mHeaderViewCount;
        private int mFooterViewCount;
        private int mMarginTop;

        public Builder(int topPadding, int bottomPadding, int startPadding, int endPadding) {
            mTopPadding = topPadding;
            mBottomPadding = bottomPadding;
            mStartPadding = startPadding;
            mEndPadding = endPadding;
        }

        public Builder setHeaderViewCount(int headerViewCount) {
            mHeaderViewCount = headerViewCount;
            return this;
        }

        public Builder setFooterViewCount(int footerViewCount) {
            mFooterViewCount = footerViewCount;
            return this;
        }

        public Builder setMarginTop(int marginTop) {
            mMarginTop = marginTop;
            return this;
        }

        public ItemSpacingDecoration build() {
            return new ItemSpacingDecoration(mTopPadding, mBottomPadding, mStartPadding, mEndPadding, mHeaderViewCount, mFooterViewCount, mMarginTop);
        }
    }
}
