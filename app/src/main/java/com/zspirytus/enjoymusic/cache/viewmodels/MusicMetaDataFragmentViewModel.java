package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.WorkerThread;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.MusicMetaDataListItem;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.global.SettingConfig;
import com.zspirytus.enjoymusic.online.RetrofitManager;
import com.zspirytus.enjoymusic.online.entity.response.SearchAlbumResponse;
import com.zspirytus.enjoymusic.online.entity.response.SearchArtistResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MusicMetaDataFragmentViewModel extends ViewModel {

    private static final String TAG = "MusicMetaDataFragmentVi";
    private MutableLiveData<List<MusicMetaDataListItem>> mMusicMetaList = new MutableLiveData<>();
    private List<MusicMetaDataListItem> dataList = new ArrayList<>();

    public MutableLiveData<List<MusicMetaDataListItem>> getMusicMetaList() {
        return mMusicMetaList;
    }

    public void obtainMusicMetaList(Music music) {
        Album album = QueryExecutor.findAlbum(music);
        Artist artist = QueryExecutor.findArtist(music);
        MusicMetaDataListItem item = new MusicMetaDataListItem();
        item.setArtistArt(true);
        item.setArtist(artist);
        dataList.add(item);

        MusicMetaDataListItem item1 = new MusicMetaDataListItem();
        item1.setMusic(true);
        item1.setMusic(music);
        dataList.add(item1);

        if (SettingConfig.isNetWorkSourceEnable) {
            MusicMetaDataListItem item2 = new MusicMetaDataListItem();
            item2.setTitle(true);
            item2.setTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_download_music_info));
            dataList.add(item2);

            MusicMetaDataListItem item3 = new MusicMetaDataListItem();
            item3.setDownloadAlbumArtView(true);
            dataList.add(item3);
        }

        MusicMetaDataListItem item4 = new MusicMetaDataListItem();
        item4.setTitle(true);
        item4.setTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_edit_info));
        dataList.add(item4);

        MusicMetaDataListItem item5 = new MusicMetaDataListItem();
        item5.setSingleEditText(true);
        item5.setEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_title));
        item5.setEditTextDefaultText(music.getMusicName());
        dataList.add(item5);

        MusicMetaDataListItem item6 = new MusicMetaDataListItem();
        item6.setSingleEditText(true);
        item6.setEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_artist));
        item6.setEditTextDefaultText(artist.getArtistName());
        dataList.add(item6);

        MusicMetaDataListItem item7 = new MusicMetaDataListItem();
        item7.setSingleEditText(true);
        item7.setEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_album));
        item7.setEditTextDefaultText(album.getAlbumName());
        dataList.add(item7);

        MusicMetaDataListItem item8 = new MusicMetaDataListItem();
        item8.setDuplicateEditText(true);
        item8.setFirstEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_disk_number));
        item8.setSecondEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_music_number));
        dataList.add(item8);

        MusicMetaDataListItem item9 = new MusicMetaDataListItem();
        item9.setDuplicateEditText(true);
        item9.setFirstEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_year));
        item9.setSecondEditTextTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_genre));
        dataList.add(item9);
        mMusicMetaList.setValue(dataList);
    }

    public void applyMusicData(Music music) {
        RetrofitManager.searchAlbum(music.getAlbum().getAlbumName(), new Observer<SearchAlbumResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchAlbumResponse searchAlbumResponse) {
                if (!searchAlbumResponse.getData().isEmpty()) {
                    updateAlbumInfo(searchAlbumResponse.getData().get(0).getAlbumPic());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
        RetrofitManager.searchArtist(music.getArtist().getArtistName(), new Observer<SearchArtistResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchArtistResponse searchArtistResponse) {
                if (!searchArtistResponse.getData().getArtists().isEmpty()) {
                    updateArtistInfo(searchArtistResponse.getData().getArtists().get(0).getPicUrl());
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @WorkerThread
    private void updateAlbumInfo(String picUrl) {
        dataList.get(1).getMusic().getAlbum().setAlbumArt(picUrl);
        AndroidSchedulers.mainThread().scheduleDirect(() -> mMusicMetaList.setValue(dataList));
    }

    @WorkerThread
    private void updateArtistInfo(String picUrl) {
        dataList.get(0).getArtist().setArtistArt(picUrl);
        AndroidSchedulers.mainThread().scheduleDirect(() -> mMusicMetaList.setValue(dataList));
    }
}
