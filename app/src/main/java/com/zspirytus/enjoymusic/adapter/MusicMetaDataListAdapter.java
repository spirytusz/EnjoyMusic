package com.zspirytus.enjoymusic.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.ArtistArt;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.engine.MusicMetaDataReader;
import com.zspirytus.enjoymusic.entity.listitem.MusicMetaData;
import com.zspirytus.enjoymusic.entity.listitem.MusicMetaDataListItem;
import com.zspirytus.enjoymusic.utils.PixelsUtil;
import com.zspirytus.enjoymusic.utils.TimeUtil;

public class MusicMetaDataListAdapter extends MultiItemAdapter<MusicMetaDataListItem> {

    private OnDownLoadBtnClickListener mListener;

    public MusicMetaDataListAdapter() {
        addArtistArtItemDelegate();
        addPreviewItemDelegate();
        addTitleItemDelegate();
        addDownloadItemDelegate();
        addSingleEditTextItemDelegate();
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
                Artist artist = data.getArtist();
                ArtistArt artistArt = QueryExecutor.findArtistArt(artist);
                String path = artistArt != null ? artistArt.getArtistArt() : null;
                if (path == null) {
                    artistArt = artist.peakArtistArt();
                    path = artistArt != null ? artistArt.getArtistArt() : null;
                }
                ImageLoader.load((ImageView) holder.getItemView(), path, data.getArtist().getArtistName(), new CenterCrop());
                holder.setOnItemClickListener((view, position) -> {
                });
            }
        };
        addDelegate(delegate);
    }

    private void addPreviewItemDelegate() {
        ItemViewDelegate<MusicMetaDataListItem> delegate = new ItemViewDelegate<MusicMetaDataListItem>() {
            @Override
            public boolean isForViewType(MusicMetaDataListItem data) {
                return data.isMusic();
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_music_preview;
            }

            @Override
            public void convert(CommonViewHolder holder, MusicMetaDataListItem data) {
                Music music = data.getMusic();
                Album album = QueryExecutor.findAlbum(music);
                Artist artist = QueryExecutor.findArtist(music);
                String path = album.getAlbumArt();
                ImageLoader.load(holder.getView(R.id.music_preview_cover), path, album.getAlbumName(), new CenterCrop());
                MusicMetaData metaData = MusicMetaDataReader.getInstance().readMetaData(music);
                String musicName = music.getMusicName();
                String artistName = artist.getArtistName();
                String albumName = album.getAlbumName();
                String duration = TimeUtil.convertLongToMinsSec(music.getMusicDuration());
                String mimeType = metaData.getMime();
                // 计算比特率及其单位
                Integer bitrate = Integer.parseInt(metaData.getBitrate());
                String[] bitratePercents = {"bps", "Kbps", "Mbps", "Gbps"};
                String bitratePercent = bitratePercents[0];
                int i = 0;
                while (bitrate > 1000 && i < bitratePercents.length - 1) {
                    bitrate /= 1000;
                    bitratePercent = bitratePercents[++i];
                }
                float sampleRate = (float) metaData.getSampleRate() / 1000;
                // 限制每行长度
                musicName = musicName.substring(0, 16 <= musicName.length() ? 16 : musicName.length());
                artistName = artistName.substring(0, 16 <= artistName.length() ? 16 : artistName.length());
                albumName = albumName.substring(0, 16 <= albumName.length() ? 16 : albumName.length());
                Spanned previewText = Html.fromHtml(
                        "<big><font color='black' style=\"line-height:150%;\">" + musicName + "</font></big><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">" + artistName + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">" + albumName + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">时长: " + duration + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">Mime: " + mimeType + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">比特率: " + bitrate + bitratePercent + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">采样率: " + sampleRate + "kHz</font><br/>"
                );
                holder.setText(R.id.music_preview_text, previewText);

                holder.setOnItemClickListener(R.id.music_preview_cover, view -> {
                });
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
                holder.setOnItemClickListener((view, position) -> {
                    if (mListener != null) {
                        mListener.onDownLoadBtnClick();
                    }
                });
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
                holder.setText(R.id.edit_text, data.getEditTextDefaultText());
            }
        };
        addDelegate(delegate);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int postion = parent.getChildAdapterPosition(view);
                MusicMetaDataListItem item = getData().get(postion);
                if (item.isTitle() || item.isSingleEditText()) {
                    outRect.left = PixelsUtil.dp2px(parent.getContext(), 18);
                }
            }
        });
    }

    public void setOnDownloadBtnClickListener(OnDownLoadBtnClickListener listener) {
        mListener = listener;
    }

    public interface OnDownLoadBtnClickListener {
        void onDownLoadBtnClick();
    }
}
