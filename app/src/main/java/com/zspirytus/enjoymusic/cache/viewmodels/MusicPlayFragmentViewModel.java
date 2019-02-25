package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.LyricLoader;
import com.zspirytus.enjoymusic.entity.LyricRow;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.online.RetrofitManager;
import com.zspirytus.enjoymusic.online.entity.response.SearchMusicResponse;
import com.zspirytus.enjoymusic.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MusicPlayFragmentViewModel extends ViewModel {

    private MutableLiveData<List<LyricRow>> mLyricRows = new MutableLiveData<>();

    public MutableLiveData<List<LyricRow>> getLyricRows() {
        return mLyricRows;
    }

    public void applyLyric(Music music) {
        RetrofitManager.searchMusic(music.getMusicName(), new Observer<SearchMusicResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchMusicResponse searchMusicResponse) {
                if (!searchMusicResponse.getData().isEmpty()) {
                    downloadLyric(searchMusicResponse.getData().get(0).getLrc());
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void downloadLyric(String lyricUrl) {
        if (lyricUrl != null) {
            ThreadPool.execute(() -> {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(lyricUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8 * 1000);
                    connection.setReadTimeout(8 * 1000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    List<String> rows = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        rows.add(line);
                    }
                    List<LyricRow> lyricRows = LyricLoader.getInstance().load(rows);
                    mLyricRows.postValue(lyricRows);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            });
        } else {
            ToastUtil.showToast(MainApplication.getForegroundContext(), "没有找到歌词...");
        }
    }
}
