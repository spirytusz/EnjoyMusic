package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.FolderSortedMusic;
import com.zspirytus.enjoymusic.entity.Music;

public class FolderSortedMusicListAdapter extends CommonRecyclerViewAdapter<FolderSortedMusic> {

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, FolderSortedMusic folderSortedMusic, int position) {
        Music firstMusicInFolder = folderSortedMusic.getFolderMusicList().get(0);
        String coverPath = firstMusicInFolder.getMusicThumbAlbumCoverPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, R.drawable.defalut_cover);
        holder.setText(R.id.item_title, folderSortedMusic.getParentFolderDir());
        holder.setText(R.id.item_sub_title, folderSortedMusic.getFolderName());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }
}
