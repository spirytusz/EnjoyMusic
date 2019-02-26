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
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.db.table.jointable.JoinMusicToSongList;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.entity.listitem.MusicPickItem;
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

    private int mSaveMusicCount;

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
            if (mListener != null && content != null && mSaveMusicCount > 0) {
                SongList songList = saveSongListToDB(content);
                mListener.onNewSongList(songList);
                dialog.dismiss();
                goBack();
            } else {
                toast("emmm...");
            }
        });
        dialog.show(getFragmentManager(), dialog.getClass().getSimpleName());
    }

    public SongList saveSongListToDB(String songListName) {
        List<Music> musicList = new ArrayList<>();
        for (MusicPickItem item : mAdapter.getList()) {
            if (item.isSelected()) {
                musicList.add(item.getMusic());
            }
        }
        SongList songList = new SongList();
        songList.setMusicCount(musicList.size());
        songList.setSongListName(songListName);
        songList.setSongListId(System.currentTimeMillis());
        List<JoinMusicToSongList> joinSongListToSongs = new ArrayList<>();
        for (Music music : musicList) {
            JoinMusicToSongList joinMusicToSongList = new JoinMusicToSongList();
            joinMusicToSongList.setMusicId(music.getMusicId());
            joinMusicToSongList.setSongListId(songList.getSongListId());
            joinSongListToSongs.add(joinMusicToSongList);
        }
        DBManager.getInstance().getDaoSession().getSongListDao().insert(songList);
        DBManager.getInstance().getDaoSession().getJoinMusicToSongListDao().insertOrReplaceInTx(joinSongListToSongs);
        return songList;
    }

    public static MusicPickFragment getInstance() {
        return new MusicPickFragment();
    }

    public interface OnSaveSongListListener {
        void onNewSongList(SongList item);
    }
}
