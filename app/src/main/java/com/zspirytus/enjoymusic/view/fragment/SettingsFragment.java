package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.SettingListAdapter;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.SettingFragmentViewModel;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.listitem.AudioEffectItem;
import com.zspirytus.enjoymusic.entity.listitem.SettingItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.utils.PixelsUtil;

import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

@LayoutIdInject(R.layout.fragment_settings_layout)
public class SettingsFragment extends CommonHeaderBaseFragment implements OnItemClickListener {

    @ViewInject(R.id.settings_recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.tool_bar)
    private Toolbar mToolbar;

    private SettingListAdapter mAdapter;
    private SettingFragmentViewModel mViewModel;
    private AnimatorSet mShadowAnim;

    public static SettingsFragment getInstance() {
        return new SettingsFragment();
    }

    @Override
    protected void initView() {
        mToolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
        mToolbar.setTitle(R.string.settings_fragment_title);
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        playShadowAnimator();
    }

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(SettingFragmentViewModel.class);
        List<SettingItem> items = mViewModel.obtainListItem();
        mAdapter = new SettingListAdapter();
        mAdapter.setData(items);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        SettingItem item = mAdapter.getData().get(position);
        if (item.isAudioEffect()) {
            AudioEffectItem audioEffectItem = item.getAudioEffectItem();
            if (audioEffectItem.isSingleEffect()) {

            } else {
                showAudioEffectFragment(audioEffectItem);
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
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

    private void showAudioEffectFragment(AudioEffectItem item) {
        AudioEffectFragment fragment = null;
        switch (item.getTitle()) {
            case "音场":
                fragment = AudioEffectFragment.getInstance(AudioEffectFragment.FLAG_AUDIO_FILED);
                break;
            case "均衡器":
                fragment = AudioEffectFragment.getInstance(AudioEffectFragment.FLAG_EQUALIZER);
                break;
            case "重低音调节器":
                fragment = AudioEffectFragment.getInstance(AudioEffectFragment.FLAG_BASS_BOAST);
                break;
        }
        FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
        FragmentVisibilityManager.getInstance().show(fragment);
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
    }
}
