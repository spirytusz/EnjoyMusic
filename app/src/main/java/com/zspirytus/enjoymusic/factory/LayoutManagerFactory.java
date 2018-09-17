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
        return createGridLayoutManager(context, spanCount, null);
    }

    public static GridLayoutManager createGridLayoutManager(Context context, int spanCount, Integer orientation) {
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount);
        if (orientation != null) {
            gridLayoutManager.setOrientation(orientation);
        }
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        return gridLayoutManager;
    }


}
