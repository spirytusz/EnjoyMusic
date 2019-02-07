package com.zspirytus.enjoymusic.adapter;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicMetaDataListItem;
import com.zspirytus.enjoymusic.utils.LogUtil;

import java.util.List;

public class MusicMetaDataListAdapter extends MultiItemAdapter<MusicMetaDataListItem> {

    public MusicMetaDataListAdapter(List<MusicMetaDataListItem> data) {
        super(data);
        addArtistArtItemDelegate();
        addPreviewItemDelegate();
        addTitleItemDelegate();
        addDownloadItemDelegate();
        addSingleEditTextItemDelegate();
        addDuplicateEditTextItemDelegate();
    }

    private void addArtistArtItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                return data.isArtistArt();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_artist_art;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
                String path = data.getPreview().getMusicThumbAlbumCoverPath();
                ImageLoader.load((ImageView) holder.getItemView(), path, R.drawable.defalut_cover, new CenterCrop());
            }
        };
        addDelegate(delegate);
    }

    private void addPreviewItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                LogUtil.e("MusicMetaDataListAdapter", "isForViewType = " + data.isPreview());
                return data.isPreview();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_music_preview;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
                Music music = data.getPreview();
                String path = music.getMusicThumbAlbumCoverPath();
                ImageLoader.load(holder.getView(R.id.music_preview_cover), path, R.drawable.defalut_cover);
                Spanned previewText = Html.fromHtml(
                        "<big><font color='black'>" + music.getMusicName() + "</font></big><br/>"
                                + "<font color='grey'>" + music.getMusicArtist() + "</font><br/>"
                                + "<font color='grey'>" + music.getMusicAlbumName() + "</font><br/>"
                );
                TextView textView = holder.getView(R.id.music_preview_text);
                textView.setText(previewText);
            }
        };
        addDelegate(delegate);
    }

    private void addTitleItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                return data.isTitle();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_title;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
                holder.setText(R.id.title, data.getTitle());
            }
        };
        addDelegate(delegate);
    }

    private void addDownloadItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                return data.isDownloadAlbumArtView();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_download_cover;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
            }
        };
        addDelegate(delegate);
    }

    private void addSingleEditTextItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                return data.isSingleEditText();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_single_edittext;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
                holder.setText(R.id.title, data.getEditTextTitle());
            }
        };
        addDelegate(delegate);
    }

    private void addDuplicateEditTextItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                return data.isDuplicateEditText();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_duplicate_edittext;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
                holder.setText(R.id.first_title, data.getFirstEditTextTitle());
                holder.setText(R.id.second_title, data.getSecondEditTextTitle());
            }
        };
        addDelegate(delegate);
    }
}
