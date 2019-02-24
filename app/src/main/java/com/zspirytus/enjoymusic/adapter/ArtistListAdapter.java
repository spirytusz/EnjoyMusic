package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;
import com.zspirytus.enjoymusic.view.dialog.SaveSongListDialog;
import com.zspirytus.enjoymusic.view.fragment.FilterAlbumListFragment;

import java.util.List;

public class ArtistListAdapter extends CommonRecyclerViewAdapter<Artist> implements OnItemLongClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Artist artist, int position) {
        holder.setImageResource(R.id.item_cover, R.drawable.defalut_cover);
        holder.setText(R.id.item_title, artist.getArtistName());
        ImageLoader.load(holder.getView(R.id.item_cover), null, artist.getArtistName());
        holder.setText(R.id.item_sub_title, artist.getNumberOfAlbums() + " 首歌曲");
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        holder.setOnItemLongClickListener(this);
        holder.getView(R.id.item_more_info_button).setOnClickListener(v -> showDialog(position));
    }

    @Override
    public void onLongClick(View view, int position) {
        showDialog(position);
    }

    private void showDialog(int position) {
        targetArtist = getList().get(position);
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(targetArtist.getArtistName(), Constant.MenuTexts.artistMenuTexts);
        dialog.setOnMenuItemClickListener(listener);
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    private Artist targetArtist;

    private PlainTextMenuDialog.OnMenuItemClickListener listener = (menuText, pos) -> {
        switch (pos) {
            case 0:
                long artistId = targetArtist.getArtistId();
                List<Music> artistFilterMusicList = DBManager.getInstance().getDaoSession()
                        .queryBuilder(Music.class).where(MusicDao.Properties.ArtistId.eq(artistId))
                        .list();
                ForegroundMusicController.getInstance().addToPlayList(artistFilterMusicList);
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
            case 2:
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(FilterAlbumListFragment.getInstance(targetArtist));
                break;
        }
    };
}
