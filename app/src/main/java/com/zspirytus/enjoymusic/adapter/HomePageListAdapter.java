package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Music;

public class HomePageListAdapter extends CommonRecyclerViewAdapter<Music> {

    @Override
    public int getLayoutId() {
        return R.layout.item_card_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Music music, int position) {
        String coverPath = music.getMusicThumbAlbumCoverPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, R.drawable.defalut_cover);
        holder.setText(R.id.item_title, music.getMusicName());
        holder.setText(R.id.item_sub_title, music.getMusicAlbumName());
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }
}
