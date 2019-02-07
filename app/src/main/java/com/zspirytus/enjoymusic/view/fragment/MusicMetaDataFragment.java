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
        mCancelBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                saveMusicMetaData();
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
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
        item2.setTitle("下载音乐信息");
        dataList.add(item2);

        MusicMetaDataListItem item3 = new MusicMetaDataListItem();
        item3.setDownloadAlbumArtView(true);
        dataList.add(item3);

        MusicMetaDataListItem item4 = new MusicMetaDataListItem();
        item4.setTitle(true);
        item4.setTitle("编辑信息");
        dataList.add(item4);

        MusicMetaDataListItem item5 = new MusicMetaDataListItem();
        item5.setSingleEditText(true);
        item5.setEditTextTitle("标题");
        dataList.add(item5);

        MusicMetaDataListItem item6 = new MusicMetaDataListItem();
        item6.setSingleEditText(true);
        item6.setEditTextTitle("艺术家");
        dataList.add(item6);

        MusicMetaDataListItem item7 = new MusicMetaDataListItem();
        item7.setSingleEditText(true);
        item7.setEditTextTitle("专辑");
        dataList.add(item7);

        MusicMetaDataListItem item8 = new MusicMetaDataListItem();
        item8.setDuplicateEditText(true);
        item8.setFirstEditTextTitle("光盘编号");
        item8.setSecondEditTextTitle("歌曲编号");
        dataList.add(item8);

        MusicMetaDataListItem item9 = new MusicMetaDataListItem();
        item9.setDuplicateEditText(true);
        item9.setFirstEditTextTitle("年");
        item9.setSecondEditTextTitle("流派");
        dataList.add(item9);

        return dataList;
    }

    private void saveMusicMetaData() {

    }

    public static MusicMetaDataFragment getInstance(Music music) {
        MusicMetaDataFragment fragment = new MusicMetaDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("music", music);
        fragment.setArguments(bundle);
        return fragment;
    }
}
