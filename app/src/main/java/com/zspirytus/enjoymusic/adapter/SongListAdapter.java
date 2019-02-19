package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.table.SongListItem;

public class SongListAdapter extends CommonRecyclerViewAdapter<SongListItem> {

    private OnItemClickListener mListener;

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, SongListItem songListItem, int position) {
        ImageLoader.load(holder.getView(R.id.item_cover), null, songListItem.getSongListName());
        holder.setText(R.id.item_title, songListItem.getSongListName());
        holder.setText(R.id.item_sub_title, songListItem.getMusicCount() + "首曲目");
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }
}
