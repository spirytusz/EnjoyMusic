package com.zspirytus.enjoymusic.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.view.activity.MainActivity;

public abstract class CommonHeaderBaseFragment extends BaseFragment {

    protected View mStatusBarView;
    protected Toolbar mToolbar;
    protected AppBarLayout mAppBarLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mStatusBarView = view.findViewById(R.id.status_bar);
        mAppBarLayout = view.findViewById(R.id.appBarLayout);
        mToolbar = view.findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationOnClickListener((v -> ((MainActivity) getParentActivity()).closeNavigationView()));
        super.onViewCreated(view, savedInstanceState);
    }

}
