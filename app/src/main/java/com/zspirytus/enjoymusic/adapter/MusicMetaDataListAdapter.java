package com.zspirytus.enjoymusic.adapter;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.zspirytus.basesdk.recyclerview.ItemViewDelegate;
import com.zspirytus.basesdk.recyclerview.adapter.MultiItemAdapter;
import com.zspirytus.basesdk.recyclerview.viewholder.CommonViewHolder;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ImageLoader;
import com.zspirytus.enjoymusic.engine.MusicMetaDataReader;
import com.zspirytus.enjoymusic.entity.MusicMetaData;
import com.zspirytus.enjoymusic.entity.MusicMetaDataListItem;
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
                String path = data.getArtist().getArtistArt();
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
                musicName = musicName.substring(0, 16 <= musicName.length() - 1 ? 16 : musicName.length() - 1);
                artistName = artistName.substring(0, 16 <= artistName.length() - 1 ? 16 : artistName.length() - 1);
                albumName = albumName.substring(0, 16 <= albumName.length() - 1 ? 16 : albumName.length() - 1);
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
                EditText editText = holder.getView(R.id.edit_text);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        data.setEditTextDefaultText(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        };
        addDelegate(delegate);
    }

    public void setOnDownloadBtnClickListener(OnDownLoadBtnClickListener listener) {
        mListener = listener;
    }

    public interface OnDownLoadBtnClickListener {
        void onDownLoadBtnClick();
    }
}
