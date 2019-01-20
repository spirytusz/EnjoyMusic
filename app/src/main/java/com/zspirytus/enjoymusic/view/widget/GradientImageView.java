package com.zspirytus.enjoymusic.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class GradientImageView extends AppCompatImageView {
    public GradientImageView(Context context) {
        this(context, null);
    }

    public GradientImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GradientImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

    }
}
