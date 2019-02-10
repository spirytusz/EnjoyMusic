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
import com.zspirytus.enjoymusic.engine.MusicMetaDataReader;
import com.zspirytus.enjoymusic.entity.Music;
import com.zspirytus.enjoymusic.entity.MusicMetaData;
import com.zspirytus.enjoymusic.entity.MusicMetaDataListItem;
import com.zspirytus.enjoymusic.utils.TimeUtil;

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
                MusicMetaData metaData = MusicMetaDataReader.getInstance().readMetaData(music);
                String musicName = music.getMusicName();
                String artist = music.getMusicArtist();
                String album = music.getMusicAlbumName();
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
                // 限制每行长度
                musicName = musicName.substring(0, 16 <= musicName.length() - 1 ? 16 : musicName.length() - 1);
                artist = artist.substring(0, 16 <= artist.length() - 1 ? 16 : artist.length() - 1);
                album = album.substring(0, 16 <= album.length() - 1 ? 16 : album.length() - 1);
                Spanned previewText = Html.fromHtml(
                        "<big><font color='black' style=\"line-height:150%;\">" + musicName + "</font></big><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">" + artist + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">" + album + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">时长: " + duration + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">Mime: " + mimeType + "</font><br/>"
                                + "<font color='grey' style=\"line-height:150%;\">比特率: " + bitrate + bitratePercent + "</font><br/>"
                );
                TextView textView = holder.getView(R.id.music_preview_text);
                textView.setText(previewText);

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
                holder.setText(R.id.first_edit_text, data.getFirstEditTextDefaultText());
                holder.setText(R.id.second_edit_text, data.getSecondEditTextDefaultText());
            }
        };
        addDelegate(delegate);
    }
}
