package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.ItemSpacingNavBarFixer;
import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.AudioEffectViewModel;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.listitem.AudioEffectItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.utils.PixelsUtil;
import com.zspirytus.enjoymusic.view.widget.EqualizerView;

@LayoutIdInject(R.layout.fragment_audio_effect)
public class AudioEffectFragment extends BaseFragment
        implements OnItemClickListener {

    private static final String FLAG_KEY = "flag";
    public static final int FLAG_AUDIO_FILED = 1;
    public static final int FLAG_EQUALIZER = 2;
    public static final int FLAG_BASS_BOAST = 3;

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
    @ViewInject(R.id.seek_bar)
    private SeekBar mBassBoaster;

    private AudioEffectViewModel mViewModel;
    private CommonRecyclerViewAdapter<AudioEffectItem> mPresetReverbAdapter;

    @Override
    protected void initData() {
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
        mViewModel = ViewModelProviders.of(this).get(AudioEffectViewModel.class);
        mViewModel.obtainDataByFlag(getArguments().getInt(FLAG_KEY));
    }

    @Override
    protected void initView() {
        mBackBtn.setOnClickListener(v -> goBack());
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mPresetReverbAdapter);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = PixelsUtil.dp2px(getContext(), 22);
            }
        });
        mRecyclerView.addItemDecoration(new ItemSpacingNavBarFixer());

        mResetBtn.setOnClickListener(v -> mEqualizer.reset());
        mEqualizer.setOnBandLevelChangeListener((band, level) -> mViewModel.setBandLevel(band, level));

        mBassBoaster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mViewModel.setBassBoastStrength(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getPresetReverbNameList().observe(this, values -> {
            mTitle.setText(R.string.audio_effect_audio_field);
            mRecyclerView.setVisibility(View.VISIBLE);
            mPresetReverbAdapter.setList(values);
        });
        mViewModel.getEqualizerMetaData().observe(this, values -> {
            mTitle.setText(R.string.audio_effect_equalizer);
            mResetBtn.setVisibility(View.VISIBLE);
            mEqualizer.setEqualizerMetaData(values);
            mEqualizer.setVisibility(View.VISIBLE);
        });
        mViewModel.getBassBoastFlag().observe(this, values -> {
            mTitle.setText(R.string.audio_effect_bass_boast);
            mBassBoaster.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        mViewModel.setPresetReverb(position);
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    public static AudioEffectFragment getInstance(int flag) {
        AudioEffectFragment fragment = new AudioEffectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FLAG_KEY, flag);
        fragment.setArguments(bundle);
        return fragment;
    }
}
