package com.zspirytus.enjoymusic.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;

import org.simple.eventbus.EventBus;

public abstract class CommonHeaderBaseFragment extends BaseFragment {

    protected AppCompatImageView mNavIcon;
    protected AppCompatTextView mTitle;
    protected View mStatusBar;
    protected View mToolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavIcon = view.findViewById(R.id.common_header_nav_btn);
        mTitle = view.findViewById(R.id.common_header_title);
        mStatusBar = view.findViewById(R.id.status_bar);
        mToolbar = view.findViewById(R.id.tool_bar);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setTitle(String text) {
        mTitle.setText(text);
    }

    public void setNavIconAction() {
        mNavIcon.setOnClickListener((view) ->
                EventBus.getDefault().post(true, Constant.EventBusTag.OPEN_DRAWER)
        );
    }

    public void setupHeaderView() {
        if (mStatusBar != null && mToolbar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mStatusBar.setBackgroundColor(Color.WHITE);
            } else {
                mStatusBar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            mToolbar.setBackgroundColor(Color.WHITE);
        }
    }
}
