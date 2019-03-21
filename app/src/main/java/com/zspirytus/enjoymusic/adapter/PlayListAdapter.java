package com.zspirytus.enjoymusic.adapter;

import android.graphics.Typeface;
import android.view.View;

import com.zspirytus.basesdk.recyclerview.adapter.SingleSelectedAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ImageLoader;

public class PlayListAdapter extends SingleSelectedAdapter<Music> {

    @Override
    public int getLayoutId() {
        return R.layout.item_common_view_type;
    }

    /**
     * 全量刷新.
     *
     * @param holder     holder
     * @param music      music
     * @param isSelected isSelected
     * @param position   position in List
     */
    @Override
    public void convertWithSelected(CommonViewHolder holder, Music music, boolean isSelected, int position) {
        Album album = QueryExecutor.findAlbum(music);
        String coverPath = album.getArtPath();
        ImageLoader.load(holder.getView(R.id.item_cover), coverPath, music.getMusicName());
        holder.setText(R.id.item_title, music.getMusicName());
        holder.setText(R.id.item_sub_title, album.getAlbumName());
        holder.setVisibility(R.id.item_more_info_button, View.GONE);
        updatePartially(holder, music, isSelected, position);
        if (mListener != null) {
            holder.setOnItemClickListener(mListener);
        }
    }

    /**
     * 只刷新选中状态, 不刷新view的内容, 如ImageView里的图片, TextView中的text都不刷新.
     *
     * @param holder     holder
     * @param music      music
     * @param isSelected isSelected
     * @param position   position in List
     */
    @Override
    public void updatePartially(CommonViewHolder holder, Music music, boolean isSelected, int position) {
        if (isSelected) {
            holder.setTypeface(R.id.item_title, Typeface.DEFAULT_BOLD);
            holder.setTypeface(R.id.item_sub_title, Typeface.DEFAULT_BOLD);
            holder.setTextColor(R.id.item_title, R.color.colorPrimary);
            holder.setTextColor(R.id.item_sub_title, R.color.colorPrimary);
        } else {
            holder.setTypeface(R.id.item_title, Typeface.DEFAULT);
            holder.setTypeface(R.id.item_sub_title, Typeface.DEFAULT_BOLD);
            holder.setTextColor(R.id.item_title, R.color.black);
            holder.setTextColor(R.id.item_sub_title, R.color.grey);
        }
    }
}
