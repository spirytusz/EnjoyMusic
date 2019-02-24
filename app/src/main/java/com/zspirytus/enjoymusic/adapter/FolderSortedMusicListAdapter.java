package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;
import com.zspirytus.enjoymusic.view.dialog.SaveSongListDialog;

public class FolderSortedMusicListAdapter extends CommonRecyclerViewAdapter<FolderSortedMusic> implements OnItemLongClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, FolderSortedMusic folderSortedMusic, int position) {
        Music firstMusicInFolder = folderSortedMusic.getFolderMusicList().get(0);
        String coverPath = firstMusicInFolder.getMusicThumbAlbumCoverPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, folderSortedMusic.getParentFolderDir());
        holder.setText(R.id.item_title, folderSortedMusic.getParentFolderDir());
        holder.setText(R.id.item_sub_title, folderSortedMusic.getFolderName());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        holder.getView(R.id.item_more_info_button).setOnClickListener(v -> showDialog(position));
    }

    @Override
    public void onLongClick(View view, int position) {
        showDialog(position);
    }

    private void showDialog(int position) {
        targetArtist = getList().get(position);
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(targetArtist.getParentFolderDir(), Constant.MenuTexts.folderMenuTexts);
        dialog.setOnMenuItemClickListener(listener);
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    private FolderSortedMusic targetArtist;

    private PlainTextMenuDialog.OnMenuItemClickListener listener = (menuText, pos) -> {
        switch (pos) {
            case 0:
                ForegroundMusicController.getInstance().addToPlayList(targetArtist.getFolderMusicList());
                ToastUtil.showToast(MainApplication.getForegroundContext(), "成功");
                break;
            case 1:
                SaveSongListDialog dialog = new SaveSongListDialog();
                dialog.setOnDialogButtonClickListener(content -> {
                    if (content != null) {
                        dialog.dismiss();
                    } else {
                        ToastUtil.showToast(MainApplication.getForegroundContext(), "emmm...");
                    }
                });
                FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
                break;
        }
    };
}
