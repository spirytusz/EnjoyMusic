package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.WorkerThread;

import com.zspirytus.enjoymusic.IMusicMetaDataUpdator;
import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.ForegroundBinderManager;
import com.zspirytus.enjoymusic.entity.listitem.MusicMetaDataListItem;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.online.RetrofitManager;
import com.zspirytus.enjoymusic.online.entity.response.SearchArtistResponse;
import com.zspirytus.enjoymusic.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MusicMetaDataFragmentViewModel extends ViewModel {

    private static final String TAG = "MusicMetaDataFragmentVi";
    private MutableLiveData<List<MusicMetaDataListItem>> mMusicMetaList = new MutableLiveData<>();
    private List<MusicMetaDataListItem> dataList = new ArrayList<>();

    private MutableLiveData<Boolean> updateState = new MutableLiveData<>();

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

        MusicMetaDataListItem item2 = new MusicMetaDataListItem();
        item2.setTitle(true);
        item2.setTitle(MainApplication.getForegroundContext().getResources().getString(R.string.music_meta_data_download_music_info));
        dataList.add(item2);

        MusicMetaDataListItem item3 = new MusicMetaDataListItem();
        item3.setDownloadAlbumArtView(true);
        dataList.add(item3);

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

        mMusicMetaList.setValue(dataList);
    }

    public void updateMusic(List<MusicMetaDataListItem> datas, MainActivityViewModel viewModel) {
        updateState.setValue(false);
        ThreadPool.execute(() -> {
            // wrap need update data.
            Artist needUpdateArtist = datas.get(0).getArtist();

            // update foreground data.
            List<Artist> artistList = viewModel.getArtistList().getValue();
            for (int i = 0; i < artistList.size(); i++) {
                if (needUpdateArtist.getArtistId().equals(artistList.get(i).getArtistId())) {
                    artistList.set(i, needUpdateArtist);
                }
            }

            // update background data.
            IBinder binder = ForegroundBinderManager.getInstance().getBinderByBinderCode(Constant.BinderCode.MUSIC_META_DATA_UPDATOR);
            IMusicMetaDataUpdator updator = IMusicMetaDataUpdator.Stub.asInterface(binder);
            try {
                updator.updateArtist(needUpdateArtist);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            updateState.postValue(true);
        });
    }

    public void applyMusicData(Music music) {
        Artist artist = QueryExecutor.findArtist(music);
        RetrofitManager.searchArtist(artist.getArtistName(), new Observer<SearchArtistResponse>() {
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
    private void updateArtistInfo(String picUrl) {
        if (picUrl != null) {
            dataList.get(0).getArtist().setArtistArt(picUrl);
            mMusicMetaList.postValue(dataList);
        } else {
            AndroidSchedulers.mainThread().scheduleDirect(() -> ToastUtil.showToast(MainApplication.getForegroundContext(), "没有找到艺术家图片..."));
        }
    }
}
