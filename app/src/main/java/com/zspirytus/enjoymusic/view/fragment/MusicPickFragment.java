package com.zspirytus.enjoymusic.view.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zspirytus.basesdk.annotations.LayoutIdInject;
import com.zspirytus.basesdk.annotations.ViewInject;
import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.basesdk.utils.ToastUtil;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseFragment;
import com.zspirytus.enjoymusic.cache.viewmodels.MainActivityViewModel;
import com.zspirytus.enjoymusic.cache.viewmodels.MusicPickFragmentViewModel;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.listitem.MusicPickItem;
import com.zspirytus.enjoymusic.factory.LayoutManagerFactory;
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

    private int mSaveMusicCount;

    private MainActivityViewModel mMainViewModel;
    private MusicPickFragmentViewModel mViewModel;
    private CommonRecyclerViewAdapter<MusicPickItem> mAdapter;

    private OnSaveSongListListener mListener;

    @Override
    protected void initData() {
        mMainViewModel = ViewModelProviders.of(getParentActivity()).get(MainActivityViewModel.class);
        mViewModel = ViewModelProviders.of(this).get(MusicPickFragmentViewModel.class);
        mAdapter = new CommonRecyclerViewAdapter<MusicPickItem>() {
            @Override
            public int getLayoutId() {
                return R.layout.item_music_pick;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicPickItem musicPickItem, int position) {
                Album album = QueryExecutor.findAlbum(musicPickItem.getMusic());
                holder.setText(R.id.item_title, musicPickItem.getMusic().getMusicName());
                holder.setText(R.id.item_sub_title, album.getAlbumName());
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
        if (mAdapter.getList().get(position).isSelected()) {
            mSaveMusicCount++;
        } else {
            mSaveMusicCount--;
        }
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
        mMainViewModel.getMusicList().observe(this, values -> {
            List<MusicPickItem> musicPickItems = new ArrayList<>();
            for (Music music : values) {
                musicPickItems.add(MusicPickItem.create(music));
            }
            mAdapter.setList(musicPickItems);
        });
    }

    @Override
    public int enterAnim() {
        return R.anim.anim_fragment_translate_show_up;
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
            if (mListener != null) {
                if (content == null || content.isEmpty()) {
                    ToastUtil.showToast(getContext(), R.string.empty_song_list_name);
                    return;
                }
                if (mSaveMusicCount > 0) {
                    ToastUtil.showToast(getContext(), R.string.empty_song_List);
                    return;
                }
                boolean isSongListDuplicate = mMainViewModel.isSongListNameDuplicate(content);
                if (!isSongListDuplicate) {
                    SongList songList = mViewModel.saveSongListToDB(content, mAdapter.getList());
                    mListener.onNewSongList(songList);
                    dialog.dismiss();
                    ToastUtil.showToast(getContext(), R.string.success);
                    goBack();
                } else {
                    ToastUtil.showToast(getContext(), R.string.duplicate_song_list_name);
                }
            }
        });
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    public static MusicPickFragment getInstance() {
        return new MusicPickFragment();
    }

    public interface OnSaveSongListListener {
        void onNewSongList(SongList item);
    }
}
