package com.zspirytus.enjoymusic.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.adapter.MusicMetaDataListAdapter;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicMetaDataListItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.view.dialog.SaveMusicInfoDialog;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.fragment_music_meta_data)
public class MusicMetaDataFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.cancel_btn)
    private ImageView mCancelBtn;
    @ViewInject(R.id.save_btn)
    private TextView mSaveBtn;

    private MusicMetaDataListAdapter mAdapter;

    @Override
    protected void initData() {
        Music music = getArguments().getParcelable("music");
        List<MusicMetaDataListItem> dataList = wrapDataList(music);
        mAdapter = new MusicMetaDataListAdapter(dataList);
    }

    @Override
    protected void initView() {
        getParentActivity().setLightStatusIconColor();
        mCancelBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        fixNavBarHeight(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                saveMusicMetaData();
                FragmentVisibilityManager.getInstance().remove(this);
                break;
            case R.id.cancel_btn:
                showDialog();
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
        showDialog();
    }

    private List<MusicMetaDataListItem> wrapDataList(Music music) {
        List<MusicMetaDataListItem> dataList = new ArrayList<>();

        MusicMetaDataListItem item = new MusicMetaDataListItem();
        item.setArtistArt(true);
        item.setPreview(music);
        dataList.add(item);

        MusicMetaDataListItem item1 = new MusicMetaDataListItem();
        item1.setPreview(true);
        item1.setPreview(music);
        dataList.add(item1);

        MusicMetaDataListItem item2 = new MusicMetaDataListItem();
        item2.setTitle(true);
        item2.setTitle(getResources().getString(R.string.music_meta_data_download_music_info));
        dataList.add(item2);

        MusicMetaDataListItem item3 = new MusicMetaDataListItem();
        item3.setDownloadAlbumArtView(true);
        dataList.add(item3);

        MusicMetaDataListItem item4 = new MusicMetaDataListItem();
        item4.setTitle(true);
        item4.setTitle(getResources().getString(R.string.music_meta_data_edit_info));
        dataList.add(item4);

        MusicMetaDataListItem item5 = new MusicMetaDataListItem();
        item5.setSingleEditText(true);
        item5.setEditTextTitle(getResources().getString(R.string.music_meta_data_title));
        item5.setEditTextDefaultText(music.getMusicName());
        dataList.add(item5);

        MusicMetaDataListItem item6 = new MusicMetaDataListItem();
        item6.setSingleEditText(true);
        item6.setEditTextTitle(getResources().getString(R.string.music_meta_data_artist));
        item6.setEditTextDefaultText(music.getMusicArtist());
        dataList.add(item6);

        MusicMetaDataListItem item7 = new MusicMetaDataListItem();
        item7.setSingleEditText(true);
        item7.setEditTextTitle(getResources().getString(R.string.music_meta_data_album));
        item7.setEditTextDefaultText(music.getMusicAlbumName());
        dataList.add(item7);

        MusicMetaDataListItem item8 = new MusicMetaDataListItem();
        item8.setDuplicateEditText(true);
        item8.setFirstEditTextTitle(getResources().getString(R.string.music_meta_data_disk_number));
        item8.setSecondEditTextTitle(getResources().getString(R.string.music_meta_data_music_number));
        dataList.add(item8);

        MusicMetaDataListItem item9 = new MusicMetaDataListItem();
        item9.setDuplicateEditText(true);
        item9.setFirstEditTextTitle(getResources().getString(R.string.music_meta_data_year));
        item9.setSecondEditTextTitle(getResources().getString(R.string.music_meta_data_genre));
        dataList.add(item9);

        return dataList;
    }

    private void saveMusicMetaData() {

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
