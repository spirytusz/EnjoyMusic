package com.zspirytus.enjoymusic.base;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.receivers.observer.PlayedMusicChangeObserver;
import com.zspirytus.enjoymusic.utils.DrawableUtil;

import org.simple.eventbus.EventBus;

public abstract class CommonHeaderBaseFragment extends BaseFragment
        implements PlayedMusicChangeObserver {

    protected AppCompatImageView mNavIcon;
    protected AppCompatTextView mTitle;
    protected AppCompatImageView mMenuIcon;
    protected AppCompatImageView mDividerLine;
    protected AppCompatTextView mATitle;
    protected AppCompatTextView mASubTitle;
    protected View mStatusBar;
    protected View mToolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout mHeaderView = view.findViewById(R.id.status_bar_toolbar_holder);
        if (mHeaderView != null) {
            mNavIcon = mHeaderView.findViewById(R.id.common_header_nav_menu_or_back_icon);
            mTitle = mHeaderView.findViewById(R.id.common_header_title);
            mMenuIcon = mHeaderView.findViewById(R.id.common_header_menu_icon);
            mStatusBar = mHeaderView.findViewById(R.id.status_bar);
            mToolbar = mHeaderView.findViewById(R.id.tool_bar);
            mDividerLine = mHeaderView.findViewById(R.id.divider_line);
            mATitle = mHeaderView.findViewById(R.id.alternative_title);
            mASubTitle = mHeaderView.findViewById(R.id.alternative_sub_title);
        }
        setTitle(getString(R.string.app_name));
    }

    @Override
    public void onPlayedMusicChanged(Music music) {
        setTitle(music.getMusicName());
    }

    public void setTitle(String text) {
        mTitle.setText(text);
    }

    public void setTitleColor(int color) {
        mTitle.setTextColor(color);
    }

    public void setNavIconAction(boolean showNavigationViewOrNot) {
        if (showNavigationViewOrNot) {
            mNavIcon.setImageResource(R.drawable.ic_menu_white_24dp);
            mNavIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(true, Constant.EventBusTag.OPEN_DRAWER);
                }
            });
        } else {
            mNavIcon.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    public void setNavIconColor(int color) {
        Drawable drawable = mNavIcon.getDrawable();
        DrawableUtil.setColor(drawable, color);
    }

    protected void setDividerLineVisibility(boolean isVisible) {
        mDividerLine.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setHeaderViewColor(int colorResId) {
        if (mStatusBar != null && mToolbar != null) {
            mStatusBar.setBackgroundColor(colorResId);
            mToolbar.setBackgroundColor(colorResId);
        }
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
        setNavIconColor(Color.parseColor("#666666"));
        mTitle.setTextColor(Color.parseColor("#666666"));
    }

    public void setMusicPlayStyle() {
        mATitle.setVisibility(View.VISIBLE);
        mASubTitle.setVisibility(View.VISIBLE);
        mTitle.setVisibility(View.GONE);
    }
}
