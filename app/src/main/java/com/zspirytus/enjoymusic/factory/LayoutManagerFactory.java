package com.zspirytus.enjoymusic.factory;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by ZSpirytus on 2018/9/17.
 */

public class LayoutManagerFactory {

    public static LinearLayoutManager createLinearLayoutManager(Context context) {
        return createLinearLayoutManager(context, null);
    }

    public static LinearLayoutManager createLinearLayoutManager(Context context, Integer orientation) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        if (orientation != null) {
            linearLayoutManager.setOrientation(orientation);
        }
        linearLayoutManager.setAutoMeasureEnabled(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        return linearLayoutManager;
    }

    public static GridLayoutManager createGridLayoutManager(Context context, int spanCount) {
        return createGridLayoutManager(context, spanCount, null, null);
    }

    public static GridLayoutManager createGridLayoutManager(Context context, final int spanCount, Integer orientation, Integer headerSpanCount) {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        if (headerSpanCount != null) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0)
                        return spanCount;
                    else
                        return 1;
                }
            });
        }
        if (orientation != null) {
            gridLayoutManager.setOrientation(orientation);
        }
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        return gridLayoutManager;
    }

    public static GridLayoutManager createGridLayoutManagerWithHeader(Context context, int spanCount, Integer headerSpanCount) {
        return createGridLayoutManager(context, spanCount, null, headerSpanCount);
    }

}
