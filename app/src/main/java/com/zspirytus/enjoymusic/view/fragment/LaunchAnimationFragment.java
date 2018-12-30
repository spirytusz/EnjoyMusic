package com.zspirytus.enjoymusic.view.fragment;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;

@LayoutIdInject(R.layout.fragment_launch_animation_layout)
public class LaunchAnimationFragment extends BaseFragment {
    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

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
