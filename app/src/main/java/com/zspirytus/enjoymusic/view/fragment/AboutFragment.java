package com.zspirytus.enjoymusic.view.fragment;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_about)
public class AboutFragment extends BaseFragment {

    public static AboutFragment getInstance() {
        AboutFragment instance = new AboutFragment();
        return instance;
    }
}
