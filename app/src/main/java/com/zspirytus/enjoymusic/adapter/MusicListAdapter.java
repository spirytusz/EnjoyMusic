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
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicFilter;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.utils.ToastUtil;
import com.zspirytus.enjoymusic.view.dialog.PlainTextMenuDialog;
import com.zspirytus.enjoymusic.view.fragment.FilterMusicListFragment;
import com.zspirytus.enjoymusic.view.fragment.MusicMetaDataFragment;

public class MusicListAdapter extends CommonRecyclerViewAdapter<Music>
        implements OnItemLongClickListener {

    private FragmentManager mManager;

    public MusicListAdapter(FragmentManager manager) {
        super();
        mManager = manager;
    }

    public MusicListAdapter() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Music music, int position) {
        String coverPath = music.getMusicThumbAlbumCoverPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, music.getMusicName());
        holder.setText(R.id.item_title, music.getMusicName());
        holder.setText(R.id.item_sub_title, music.getMusicAlbumName());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        if (mManager != null) {
            holder.setOnItemLongClickListener(this);
            holder.setOnItemClickListener(R.id.item_more_info_button, v -> {
                showDialog(position);
            });
        }
    }

    @Override
    public void onLongClick(View view, int position) {
        showDialog(position);
    }

    private void showDialog(int position) {
        targetMusic = getList().get(position);
        PlainTextMenuDialog dialog = PlainTextMenuDialog.create(targetMusic.getMusicName(), Constant.MenuTexts.menuTexts);
        dialog.setOnMenuItemClickListener(listener);
        dialog.show(mManager);
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
                MusicFilter filterForAlbum = new MusicFilter(targetMusic.getMusicAlbumName(), null);
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(FilterMusicListFragment.getInstance(filterForAlbum));
                break;
            case 3:
                MusicFilter filterForArtist = new MusicFilter(null, targetMusic.getMusicArtist());
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(FilterMusicListFragment.getInstance(filterForArtist));
                break;
            case 4:
                FragmentVisibilityManager.getInstance().addCurrentFragmentToBackStack();
                FragmentVisibilityManager.getInstance().show(MusicMetaDataFragment.getInstance(targetMusic));
                break;
        }
    };
}
