package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.animation.DecelerateInterpolator;

import com.zspirytus.enjoymusic.IEqualizerHelper;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.entity.EqualizerMetaData;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.utils.PixelsUtil;
import com.zspirytus.enjoymusic.view.widget.EqualizerView;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_about_layout)
public class AboutFragment extends CommonHeaderBaseFragment {

    @ViewInject(R.id.equalizer_view)
    private EqualizerView mEqualizerView;
    private AnimatorSet mShadowAnim;

    public static AboutFragment getInstance() {
        AboutFragment instance = new AboutFragment();
        return instance;
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mToolbar.setTitle(R.string.about_fragment_title);
        mEqualizerView.setOnBandLevelChangeListener((band, level) -> {
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.EQUALIZER_HELPER);
            IEqualizerHelper equalizerHelper = IEqualizerHelper.Stub.asInterface(binder);
            try {
                equalizerHelper.setBandLevel(band, level);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
        playShadowAnimator();
    }

    @Override
    protected void initData() {
        IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.EQUALIZER_HELPER);
        IEqualizerHelper equalizerHelper = IEqualizerHelper.Stub.asInterface(binder);
        try {
            EqualizerMetaData metaData = equalizerHelper.addEqualizerSupport();
            mEqualizerView.setEqualizerMetaData(metaData);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            playShadowAnimator();
        } else {
            if (mShadowAnim.isRunning()) {
                mShadowAnim.cancel();
            }
            mAppBarLayout.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mAppBarLayout.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setElevation(PixelsUtil.dp2px(getContext(), 0));
            mStatusBarView.setTranslationZ(PixelsUtil.dp2px(getContext(), 0));
        }
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
    }

    private void initAnim() {
        mShadowAnim = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mAppBarLayout, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mAppBarLayout, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(mStatusBarView, "elevation", 0, PixelsUtil.dp2px(getContext(), 6));
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(mStatusBarView, "translationZ", 0, PixelsUtil.dp2px(getContext(), 4));
        mShadowAnim = new AnimatorSet();
        mShadowAnim.playTogether(animator, animator1, animator2, animator3);
        mShadowAnim.setInterpolator(new DecelerateInterpolator());
        mShadowAnim.setDuration(618);
    }

    private void playShadowAnimator() {
        if (mShadowAnim == null) {
            initAnim();
        }
        mShadowAnim.start();
    }
}
