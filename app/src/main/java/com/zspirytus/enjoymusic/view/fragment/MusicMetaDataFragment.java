package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicMetaDataListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MusicMetaDataFragmentViewModel;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.view.dialog.ProgressDialog;
import com.zspirytus.enjoymusic.view.dialog.SaveMusicInfoDialog;

@LayoutIdInject(R.layout.fragment_music_meta_data)
public class MusicMetaDataFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.cancel_btn)
    private ImageView mCancelBtn;
    @ViewInject(R.id.save_btn)
    private TextView mSaveBtn;
    @ViewInject(R.id.progress_bar)
    private ProgressBar mProgressBar;

    private MusicMetaDataFragmentViewModel mViewModel;
    private MusicMetaDataListAdapter mAdapter;

    private ProgressDialog mProgressDialog;

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(this).get(MusicMetaDataFragmentViewModel.class);
        Music music = getArguments().getParcelable("music");
        mViewModel.applyMusicMetaList(music);
        mAdapter = new MusicMetaDataListAdapter();
        mAdapter.setOnDownloadBtnClickListener(new MusicMetaDataListAdapter.OnClickEventListener() {
            @Override
            public void onAritstArtLongClickListener() {
                mViewModel.applyArtistArt(music);
            }

            @Override
            public void onAlbumArtLongClickListener() {
                mViewModel.applyAlbumArt(music);
            }
        });
    }

    @Override
    protected void initView() {
        mCancelBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        fixNavBarHeight(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getMusicMetaList().observe(this, values -> {
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.setData(values);
        });
        mViewModel.getUpdateState().observe(this, values -> {
            if (values != null && !values) {
                if (mProgressDialog == null) {
                    mProgressDialog = ProgressDialog.getInstance();
                }
                FragmentVisibilityManager.getInstance().showDialogFragment(mProgressDialog);
            } else {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                FragmentVisibilityManager.getInstance().remove(this);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                MainActivityViewModel viewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
                mViewModel.updateMusic(mAdapter.getData(), viewModel);
                break;
            case R.id.cancel_btn:
                goBack();
                break;
        }
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    @Override
    public int enterAnim() {
        return R.anim.anim_fragment_translate_show_up;
    }

    @Override
    public int exitAnim() {
        return super.exitAnim();
    }

    @Override
    public void goBack() {
        if (mViewModel.hasUpdate()) {
            showDialog();
        } else {
            FragmentVisibilityManager.getInstance().remove(MusicMetaDataFragment.this);
        }
    }

    private void showDialog() {
        SaveMusicInfoDialog dialog = new SaveMusicInfoDialog();
        dialog.setOnDialogButtonClickListener(new SaveMusicInfoDialog.OnDialogButtonClickListener() {
            @Override
            public void onPositiveBtnClick() {
                FragmentVisibilityManager.getInstance().remove(MusicMetaDataFragment.this);
            }

            @Override
            public void onNegativeBtnClick() {
            }
        });
        dialog.show(getChildFragmentManager(), dialog.getClass().getSimpleName());
    }

    public static MusicMetaDataFragment getInstance(Music music) {
        MusicMetaDataFragment fragment = new MusicMetaDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", music);
        fragment.setArguments(bundle);
        return fragment;
    }
}
