package com.zspirytus.enjoymusic.adapter;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.listeners.OnItemClickListener;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.table.SongList;
import com.zspirytus.enjoymusic.engine.ImageLoader;

public class SongListAdapter extends CommonRecyclerViewAdapter<SongList> {

    private OnItemClickListener mListener;

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, SongList songList, int position) {
        ImageLoader.load(holder.getView(R.id.item_cover), null, songList.getSongListName());
        holder.setText(R.id.item_title, songList.getSongListName());
        holder.setText(R.id.item_sub_title, songList.getMusicCount() + "首曲目");
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        mListener = l;
    }
}
