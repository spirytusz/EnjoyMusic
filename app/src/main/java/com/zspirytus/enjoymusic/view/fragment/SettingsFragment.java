package com.zspirytus.enjoymusic.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Switch;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.CommonHeaderBaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.SettingFragmentViewModel;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.SettingItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.global.SettingConfig;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
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

    private CommonRecyclerViewAdapter<SettingItem> mAdapter;
    private SettingFragmentViewModel mViewModel;
    private AnimatorSet mShadowAnim;

    public static SettingsFragment getInstance() {
        SettingsFragment instance = new SettingsFragment();
        return instance;
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
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
        mAdapter = new CommonRecyclerViewAdapter<SettingItem>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_simple;
            }

            @Override
            public void convert(CommonViewHolder holder, SettingItem item, int position) {
                holder.setVisibility(R.id.item_image, View.GONE);
                if (item.isHasSwitch()) {
                    Switch check = holder.getView(R.id.item_switch);
                    check.setOnClickListener(v -> {
                        SettingConfig.isNetWorkSourceEnable = !SettingConfig.isNetWorkSourceEnable;
                        boolean isChecked = SettingConfig.isNetWorkSourceEnable;
                        mAdapter.getList().get(position).setChecked(isChecked);
                        check.setChecked(isChecked);
                    });
                    check.setVisibility(View.VISIBLE);
                    check.setChecked(item.isChecked());
                    holder.setText(R.id.item_text, item.getTitle());
                } else {
                    holder.setVisibility(R.id.item_switch, View.GONE);
                    holder.setText(R.id.item_text, item.getTitle());
                }
                holder.setOnItemClickListener(SettingsFragment.this);
            }
        };
        mAdapter.setList(items);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position == 0) {
            AudioEffectFragment fragment = AudioEffectFragment.getInstance();
            FragmentVisibilityManager.getInstance().addToBackStack(this);
            FragmentVisibilityManager.getInstance().show(fragment);
        } else if (position == 1) {
            SettingConfig.isNetWorkSourceEnable = !SettingConfig.isNetWorkSourceEnable;
            boolean isChecked = SettingConfig.isNetWorkSourceEnable;
            mAdapter.getList().get(position).setChecked(isChecked);
            Switch checked = view.findViewById(R.id.item_switch);
            checked.setChecked(isChecked);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getParentActivity().setLightStatusIconColor();
            playShadowAnimator();
        } else {
            getParentActivity().setDefaultStatusIconColor();
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

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    protected void onLoadState(boolean isSuccess) {
    }
}
