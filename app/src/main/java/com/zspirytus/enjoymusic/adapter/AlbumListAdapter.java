package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Album;

public class AlbumListAdapter extends CommonRecyclerViewAdapter<Album> {

    @Override
    public int getLayoutId() {
        return R.layout.item_card_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Album album, int position) {
        String coverPath = album.getAlbumCoverPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, R.drawable.defalut_cover);
        holder.setText(R.id.item_title, album.getAlbumName());
        holder.setText(R.id.item_sub_title, album.getArtist());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }
}
