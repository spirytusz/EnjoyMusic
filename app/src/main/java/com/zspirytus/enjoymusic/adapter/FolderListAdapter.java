package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Folder;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ImageLoader;

import java.util.List;

public class FolderListAdapter extends CommonRecyclerViewAdapter<Folder> {

    private OnMoreInfoBtnClickListener mOnMoreInfoBtnClickListener;

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Folder folder, int position) {
        List<Music> musicList = QueryExecutor.findMusicList(folder);
        Music firstMusicInFolder = musicList.get(0);
        Album album = QueryExecutor.findAlbum(firstMusicInFolder);
        String coverPath = album.getArtPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, folder.getFolderName());
        holder.setText(R.id.item_title, folder.getFolderName());
        holder.setText(R.id.item_sub_title, folder.getFolderDir());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
        if (mLongClickListener != null) {
            holder.setOnItemLongClickListener(mLongClickListener);
        }
        holder.getView(R.id.item_more_info_button).setOnClickListener(
                v -> mOnMoreInfoBtnClickListener.onMoreInfoBtnClick(
                        holder.getView(R.id.item_more_info_button),
                        holder.getAdapterPosition()
                )
        );
    }

    public void setOnMoreInfoBtnClickListener(OnMoreInfoBtnClickListener onMoreInfoBtnClickListener) {
        mOnMoreInfoBtnClickListener = onMoreInfoBtnClickListener;
    }

    public interface OnMoreInfoBtnClickListener {
        void onMoreInfoBtnClick(View v, int position);
    }
}
