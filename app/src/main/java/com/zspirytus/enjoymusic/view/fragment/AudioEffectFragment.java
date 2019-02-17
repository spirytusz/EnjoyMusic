package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.ItemSpacingNavBarFixer;
import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.AudioEffectViewModel;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.AudioEffectItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.view.widget.EqualizerView;

@LayoutIdInject(R.layout.fragment_audio_effect)
public class AudioEffectFragment extends BaseFragment
        implements OnItemClickListener, View.OnClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.equalizer_view)
    private EqualizerView mEqualizer;
    @ViewInject(R.id.back_btn)
    private ImageView mBackBtn;
    @ViewInject(R.id.title)
    private TextView mTitle;
    @ViewInject(R.id.reset_btn)
    private TextView mResetBtn;

    private AudioEffectViewModel mViewModel;
    private CommonRecyclerViewAdapter<AudioEffectItem> mAdapter;
    private CommonRecyclerViewAdapter<AudioEffectItem> mPresetReverbAdapter;

    @Override
    protected void initData() {
        mAdapter = new CommonRecyclerViewAdapter<AudioEffectItem>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_simple;
            }

            @Override
            public void convert(CommonViewHolder holder, AudioEffectItem item, int position) {
                holder.setText(R.id.item_text, item.getTitle());
                if (item.isSingleEffect()) {
                    holder.setVisibility(R.id.item_switch, View.VISIBLE);
                    Switch checked = holder.getView(R.id.item_switch);
                    checked.setChecked(item.isChecked());
                }
                holder.setOnItemClickListener(AudioEffectFragment.this);
            }
        };
        mPresetReverbAdapter = new CommonRecyclerViewAdapter<AudioEffectItem>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_simple;
            }

            @Override
            public void convert(CommonViewHolder holder, AudioEffectItem audioEffectItem, int position) {
                holder.setText(R.id.item_text, audioEffectItem.getTitle());
                holder.setVisibility(R.id.item_image, audioEffectItem.isChecked() ? View.VISIBLE : View.GONE);
                holder.setOnItemClickListener(AudioEffectFragment.this);
            }
        };
        mRecyclerView.addItemDecoration(new ItemSpacingNavBarFixer());
        mEqualizer.setOnBandLevelChangeListener((band, level) -> {
            mViewModel.setBandLevel(band, level);
        });
        mViewModel = ViewModelProviders.of(this).get(AudioEffectViewModel.class);
        mViewModel.obtainAudioEffects();
        mViewModel.obtainPresetReverbList();
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mBackBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getAudioEffects().observe(this, values -> {
            mAdapter.setList(values);
        });
        mViewModel.getPresetReverbNameList().observe(this, values -> {
            mPresetReverbAdapter.setList(values);
        });
        mViewModel.getEqualizerMetaData().observe(this, values -> {
            mEqualizer.setEqualizerMetaData(values);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mRecyclerView.getAdapter() == mAdapter) {
            if (position == mAdapter.getItemCount() - 1) {
                mTitle.setText("音场");
                mRecyclerView.setAdapter(mPresetReverbAdapter);
            } else {
                boolean isChecked = mAdapter.getList().get(position).isChecked();
                mAdapter.getList().get(position).setChecked(!isChecked);
                Switch check = view.findViewById(R.id.item_switch);
                check.setChecked(!isChecked);
                mViewModel.setEffectEnable(!isChecked, position);
            }
        } else {
            for (int i = 0; i < mPresetReverbAdapter.getList().size(); i++) {
                if (mPresetReverbAdapter.getList().get(i).isChecked()) {
                    mPresetReverbAdapter.getList().get(i).setChecked(false);
                    break;
                }
            }
            mPresetReverbAdapter.getList().get(position).setChecked(true);
            mPresetReverbAdapter.notifyDataSetChanged();
            if (position != mPresetReverbAdapter.getItemCount() - 1) {
                mViewModel.setPresetReverb(position);
            } else {
                showEqualizer();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                goBack();
                break;
            case R.id.reset_btn:
                mEqualizer.reset();
                break;
        }
    }

    @Override
    public void goBack() {
        if (mEqualizer.getVisibility() == View.VISIBLE) {
            hideEqualizer();
        } else if (mRecyclerView.getAdapter() == mPresetReverbAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            FragmentVisibilityManager.getInstance().remove(this);
        }
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_container;
    }

    private void showEqualizer() {
        if (!mViewModel.isEqualizerInited()) {
            mViewModel.obtainEqualizerMetaData();
        }
        mRecyclerView.setVisibility(View.GONE);
        mEqualizer.setVisibility(View.VISIBLE);
        mTitle.setText("均衡器");
        mResetBtn.setVisibility(View.VISIBLE);
    }

    private void hideEqualizer() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEqualizer.setVisibility(View.GONE);
        mTitle.setText("音场");
        mResetBtn.setVisibility(View.GONE);
    }

    public static AudioEffectFragment getInstance() {
        return new AudioEffectFragment();
    }
}
