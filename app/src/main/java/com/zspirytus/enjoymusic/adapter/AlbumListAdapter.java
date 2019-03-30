package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.engine.ImageLoader;

public class AlbumListAdapter extends CommonRecyclerViewAdapter<Album> {

    private OnMoreInfoBtnClickListener mOnMoreInfoBtnClickListener;

    @Override
    public int getLayoutId() {
        return R.layout.item_card_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Album album, int position) {
        Artist artist = QueryExecutor.findArtist(album);
        String coverPath = album.getArtPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, album.getAlbumName());
        holder.setText(R.id.item_title, album.getAlbumName());
        holder.setText(R.id.item_sub_title, artist != null ? artist.getArtistName() : "artist");
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
