package com.zspirytus.enjoymusic.view.fragment;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.interfaces.LayoutIdInject;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_settings)
public class SettingsFragment extends BaseFragment {

    public static SettingsFragment getInstance() {
        SettingsFragment instance = new SettingsFragment();
        return instance;
    }
}
