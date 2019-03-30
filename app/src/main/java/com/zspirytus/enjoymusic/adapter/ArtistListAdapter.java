package com.zspirytus.enjoymusic.adapter;

import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.CommonRecyclerViewAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.engine.ImageLoader;

public class ArtistListAdapter extends CommonRecyclerViewAdapter<Artist> {

    private OnMoreInfoBtnClickListener mOnMoreInfoBtnClickListener;

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    @Override
    public void convert(CommonViewHolder holder, Artist artist, int position) {
        ArtistArt artistArt = QueryExecutor.findArtistArt(artist);
        String path = artistArt != null ? artistArt.getArtistArt() : null;
        ImageLoader.load(holder.getView(R.id.item_cover), path, artist.getArtistName());
        holder.setText(R.id.item_title, artist.getArtistName());
        holder.setText(R.id.item_sub_title, artist.getNumberOfAlbums() + " 首歌曲");
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
