package com.zspirytus.enjoymusic.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;

import org.simple.eventbus.EventBus;

public abstract class CommonHeaderBaseFragment extends BaseFragment {

    protected Toolbar mToolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mToolbar = view.findViewById(R.id.tool_bar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationOnClickListener((v -> EventBus.getDefault().post(true, Constant.EventBusTag.OPEN_DRAWER)));
    }

}
