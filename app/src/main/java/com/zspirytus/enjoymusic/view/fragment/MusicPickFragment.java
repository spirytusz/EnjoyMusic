package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicPickItem;
import com.zspirytus.enjoymusic.entity.table.SongListItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
import com.zspirytus.enjoymusic.interfaces.annotations.LayoutIdInject;
import com.zspirytus.enjoymusic.interfaces.annotations.ViewInject;
import com.zspirytus.enjoymusic.view.dialog.SaveSongListDialog;

import java.util.ArrayList;
import java.util.List;

@LayoutIdInject(R.layout.fragment_music_pick)
public class MusicPickFragment extends BaseFragment
        implements OnItemClickListener, View.OnClickListener {

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.back_btn)
    private ImageView mBackBtn;
    @ViewInject(R.id.save_btn)
    private TextView mSaveBtn;

    private MainActivityViewModel mViewModel;
    private CommonRecyclerViewAdapter<MusicPickItem> mAdapter;

    private OnSaveSongListListener mListener;

    @Override
    protected void initData() {
        mViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
        mAdapter = new CommonRecyclerViewAdapter<MusicPickItem>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_music_pick;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicPickItem musicPickItem, int position) {
                holder.setText(R.id.item_title, musicPickItem.getMusic().getMusicName());
                holder.setText(R.id.item_sub_title, musicPickItem.getMusic().getMusicAlbumName());
                CheckBox checkBox = holder.getView(R.id.item_checkbox);
                checkBox.setChecked(musicPickItem.isSelected());
                holder.setOnItemClickListener(MusicPickFragment.this);
            }
        };
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(LayoutManagerFactory.createLinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mBackBtn.setOnClickListener(this);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        CheckBox checkBox = view.findViewById(R.id.item_checkbox);
        mAdapter.getList().get(position).setSelected(!mAdapter.getList().get(position).isSelected());
        checkBox.setChecked(mAdapter.getList().get(position).isSelected());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                goBack();
                break;
            case R.id.save_btn:
                saveSongList();
                break;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.getMusicList().observe(getParentActivity(), values -> {
            List<MusicPickItem> musicPickItems = new ArrayList<>();
            for (Music music : values) {
                musicPickItems.add(MusicPickItem.create(music));
            }
            mAdapter.setList(musicPickItems);
        });
    }

    @Override
    public void goBack() {
        FragmentVisibilityManager.getInstance().remove(this);
    }

    @Override
    public int getContainerId() {
        return R.id.full_fragment_container;
    }

    public void setOnSaveSongListListener(OnSaveSongListListener l) {
        mListener = l;
    }

    private void saveSongList() {
        SaveSongListDialog dialog = new SaveSongListDialog();
        dialog.setOnDialogButtonClickListener(content -> {
            List<Music> musicList = new ArrayList<>();
            for (MusicPickItem item : mAdapter.getList()) {
                if (item.isSelected()) {
                    musicList.add(item.getMusic());
                }
            }
            SongListItem item = new SongListItem();
            item.setMusicCount(musicList.size());
            item.setSongListName(content);
            item.setMusicList(musicList);
            item.save();
            // TODO: 20/02/2019 Save MusicList.
            if (mListener != null) {
                mListener.onNewSongList(item);
            }
            goBack();
        });
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    public static MusicPickFragment getInstance() {
        return new MusicPickFragment();
    }

    public interface OnSaveSongListListener {
        void onNewSongList(SongListItem item);
    }
}
