package com.zspirytus.enjoymusic.adapter;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;
import com.zspirytus.enjoymusic.view.dialog.SaveSongListDialog;
import com.zspirytus.enjoymusic.view.fragment.FilterMusicListFragment;

public class AlbumListAdapter extends CommonRecyclerViewAdapter<Album>
        implements OnItemLongClickListener {

    private FragmentManager mManager;

    public AlbumListAdapter(FragmentManager manager) {
        super();
        mManager = manager;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_card_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Album album, int position) {
        String coverPath = album.getAlbumCoverPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, album.getAlbumName());
        holder.setText(R.id.item_title, album.getAlbumName());
        holder.setText(R.id.item_sub_title, album.getArtist());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        holder.setOnItemLongClickListener(this);
        holder.getView(R.id.item_more_info_button).setOnLongClickListener(v -> {
            showDialog(position);
            return true;
        });
    }

    @Override
    public void onLongClick(View view, int position) {
        showDialog(position);
    }

    private void showDialog(int position) {
        targetAlbum = getList().get(position);
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(targetAlbum.getAlbumName(), Constant.MenuTexts.albumMenuTexts);
        dialog.setOnMenuItemClickListener(listener);
        dialog.show(mManager);
    }

    private Album targetAlbum;

    private PlainTextMenuDialog.OnMenuItemClickListener listener = (menuText, pos) -> {
        switch (pos) {
            case 0:
                MusicFilter filterForAlbum = new MusicFilter(targetAlbum.getAlbumName(), null);
                ForegroundMusicController.getInstance().addToPlayList(filterForAlbum);
                ToastUtil.showToast(MainApplication.getForegroundContext(), "成功");
                break;
            case 1:
                MusicFilter filterArtist = new MusicFilter(null, targetAlbum.getArtist());
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(FilterMusicListFragment.getInstance(filterArtist));
                break;
            case 2:
                SaveSongListDialog dialog = new SaveSongListDialog();
                dialog.setOnDialogButtonClickListener(content -> {
                    if (content != null) {
                        dialog.dismiss();
                    } else {
                        ToastUtil.showToast(MainApplication.getForegroundContext(), "emmm...");
                    }
                });
                break;
        }
    };
}
