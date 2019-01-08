package com.zspirytus.enjoymusic.view.fragment;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_about_layout)
public class AboutFragment extends CommonHeaderBaseFragment {

    public static AboutFragment getInstance() {
        AboutFragment instance = new AboutFragment();
        return instance;
    }

    @Override
    protected void initView() {
        setNavIconAction(true);
        setupHeaderView();
    }

    @Override
    protected void initData() {
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
    }
}
