package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;

import java.util.List;

public class SongListAdapter extends CommonRecyclerViewAdapter<SongList> implements OnItemLongClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, SongList songList, int position) {
        ImageLoader.load(holder.getView(R.id.item_cover), null, songList.getSongListName());
        holder.setText(R.id.item_title, songList.getSongListName());
        holder.setText(R.id.item_sub_title, songList.getMusicCount() + "首曲目");
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        holder.setOnItemClickListener(R.id.item_more_info_button, v -> showDialog(position));
        holder.setOnItemLongClickListener(this);
    }

    @Override
    public void onLongClick(View view, int position) {
        showDialog(position);
    }

    private void showDialog(int position) {
        targetSongList = getList().get(position);
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(targetSongList.getSongListName(), Constant.MenuTexts.songListMenuTexts);
        dialog.setOnMenuItemClickListener(listener);
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    private SongList targetSongList;

    private PlainTextMenuDialog.OnMenuItemClickListener listener = (menuText, pos) -> {
        switch (pos) {
            case 0:
                List<Music> musicList = targetSongList.getSongsOfThisSongList();
                ForegroundMusicController.getInstance().addToPlayList(musicList);
                ToastUtil.showToast(MainApplication.getForegroundContext(), "成功");
                break;
        }
    };
}
