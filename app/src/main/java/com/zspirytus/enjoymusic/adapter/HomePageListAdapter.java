package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemLongClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.greendao.MusicDao;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.engine.FragmentVisibilityManager;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;
import com.zspirytus.enjoymusic.view.fragment.FilterMusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicMetaDataFragment;

import java.util.List;

public class HomePageListAdapter extends CommonRecyclerViewAdapter<Music> implements OnItemLongClickListener {

    @Override
    public int getLayoutId() {
        return R.layout.item_card_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Music music, int position) {
        Album album = QueryExecutor.findAlbum(music);
        String coverPath = album.getAlbumArt();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, music.getMusicName());
        holder.setText(R.id.item_title, music.getMusicName());
        holder.setText(R.id.item_sub_title, album.getAlbumName());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        holder.setOnItemLongClickListener(this);
        holder.setOnItemClickListener(R.id.item_more_info_button, v -> {
            showDialog(position);
        });
    }

    @Override
    public void onLongClick(View view, int position) {
        showDialog(position - 1);
    }

    private void showDialog(int position) {
        targetMusic = getList().get(position);
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(targetMusic.getMusicName(), Constant.MenuTexts.menuTexts);
        dialog.setOnMenuItemClickListener(listener);
        FragmentVisibilityManager.getInstance().showDialogFragment(dialog);
    }

    private Music targetMusic;

    private PlainTextMenuDialog.OnMenuItemClickListener listener = (menuText, pos) -> {
        switch (pos) {
            case 0:
                ForegroundMusicController.getInstance().addToPlayList(targetMusic);
                ToastUtil.showToast(MainApplication.getForegroundContext(), "成功");
                break;
            case 1:
                ToastUtil.showToast(MainApplication.getForegroundContext(), "delete it.");
                break;
            case 2:
                long albumId = targetMusic.getAlbumId();
                List<Music> songs = DBManager.getInstance().getDaoSession().queryBuilder(Music.class)
                        .where(MusicDao.Properties.AlbumId.eq(albumId))
                        .list();
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(FilterMusicListFragment.getInstance(targetMusic.getAlbum().getAlbumName(), songs, 1));
                break;
            case 3:
                long artistId = targetMusic.getArtistId();
                List<Music> songs1 = DBManager.getInstance().getDaoSession().queryBuilder(Music.class)
                        .where(MusicDao.Properties.ArtistId.eq(artistId))
                        .list();
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(FilterMusicListFragment.getInstance(targetMusic.getArtist().getArtistName(), songs1, 2));
                break;
            case 4:
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(MusicMetaDataFragment.getInstance(targetMusic));
                break;
        }
    };
}
