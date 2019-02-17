package com.zspirytus.basesdk.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zspirytus.basesdk.utils.PixelsUtil;

public class ItemSpacingNavBarFixer extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int postion = parent.getChildAdapterPosition(view);
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
            int span = manager.getSpanCount();
            if (postion >= parent.getAdapter().getItemCount() - 1 - span) {
                outRect.bottom = PixelsUtil.getNavigationBarHeight(view.getContext());
            }
        } else if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            if (postion == parent.getAdapter().getItemCount() - 1) {
                outRect.bottom = PixelsUtil.getNavigationBarHeight(view.getContext());
            }
        }
    }
}
