package com.zspirytus.enjoymusic.view.fragment;

import android.support.v7.widget.RecyclerView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_settings_layout)
public class SettingsFragment extends CommonHeaderBaseFragment {

    @ViewInject(R.id.settings_recyclerview)
    private RecyclerView mSettingFragmentRecyclerView;

    public static SettingsFragment getInstance() {
        SettingsFragment instance = new SettingsFragment();
        return instance;
    }

    @Override
    protected void initView() {
        setNavIconAction(true);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
    }

    @Override
    public void goBack() {
        long now = System.currentTimeMillis();
        if (now - pressedBackLastTime < 2 * 1000) {
            getParentActivity().finish();
        } else {
            toast("Press back again to quit");
            pressedBackLastTime = now;
        }
    }
}
