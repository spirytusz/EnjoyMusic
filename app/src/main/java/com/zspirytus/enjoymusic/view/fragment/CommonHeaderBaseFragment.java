package com.zspirytus.enjoymusic.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zspirytus.enjoymusic.R;

public class CommonHeaderBaseFragment extends BaseFragment {

    private ConstraintLayout mHeaderView;

    private AppCompatImageView mNavIcon;
    private AppCompatTextView mTitle;
    private AppCompatImageView mMenuIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mHeaderView = view.findViewById(R.id.status_bar_toolbar_holder);
        mNavIcon = mHeaderView.findViewById(R.id.common_header_nav_menu_or_back_icon);
        mTitle = mHeaderView.findViewById(R.id.common_header_title);
        mMenuIcon = mHeaderView.findViewById(R.id.common_header_menu_icon);
        return view;
    }

    public void setTitle(String text) {
        mTitle.setText(text);
    }

    public void setNavIconAction(boolean showNavigationViewOrNot) {
        if (showNavigationViewOrNot) {
            mNavIcon.setImageResource(R.drawable.ic_menu_white_24dp);
        } else {
            mNavIcon.setImageResource(R.drawable.ic_arrow_back_white_24dp);
        }
    }
}
