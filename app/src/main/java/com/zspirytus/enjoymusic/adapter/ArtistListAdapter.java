package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.entity.Artist;

public class ArtistListAdapter extends CommonRecyclerViewAdapter<Artist> {

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Artist artist, int position) {
        holder.setImageResource(R.id.item_cover, R.drawable.defalut_cover);
        holder.setText(R.id.item_title, artist.getArtistName());
        holder.setText(R.id.item_sub_title, artist.getNumberOfAlbums() + " 首歌曲");
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }
}
