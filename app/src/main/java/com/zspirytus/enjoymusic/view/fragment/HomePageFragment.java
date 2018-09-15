package com.zspirytus.enjoymusic.view.fragment;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_home_page)
public class HomePageFragment extends BaseFragment {

    public int getPriority() {
        return 56789;
    }

    protected static HomePageFragment getInstance() {
        HomePageFragment instance = new HomePageFragment();
        return instance;
    }
}
