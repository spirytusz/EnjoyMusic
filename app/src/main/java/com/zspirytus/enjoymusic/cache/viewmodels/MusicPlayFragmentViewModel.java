package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.WorkerThread;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.cache.ThreadPool;
import com.zspirytus.enjoymusic.db.DBManager;
import com.zspirytus.enjoymusic.db.QueryExecutor;
import com.zspirytus.enjoymusic.db.greendao.LyricDao;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Lyric;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.engine.LyricLoader;
import com.zspirytus.enjoymusic.engine.MinEditDistance;
import com.zspirytus.enjoymusic.entity.LyricRow;
import com.zspirytus.enjoymusic.global.MainApplication;
import com.zspirytus.enjoymusic.online.RetrofitManager;
import com.zspirytus.enjoymusic.online.entity.OnlineMusic;
import com.zspirytus.enjoymusic.online.entity.response.SearchMusicResponse;
import com.zspirytus.enjoymusic.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MusicPlayFragmentViewModel extends ViewModel {

    private MutableLiveData<List<LyricRow>> mLyricRows = new MutableLiveData<>();

    public MutableLiveData<List<LyricRow>> getLyricRows() {
        return mLyricRows;
    }

    public void applyLyricFromDB(Music music) {
        ThreadPool.execute(() -> {
            List<Lyric> lyrics = DBManager.getInstance().getDaoSession().getLyricDao().queryBuilder()
                    .where(LyricDao.Properties.MusicId.eq(music.getMusicId()))
                    .list();
            if (!lyrics.isEmpty()) {
                String path = lyrics.get(0).getLyricFilePath();
                File lyricFile = new File(path);
                List<LyricRow> lyricRows = LyricLoader.getInstance().load(lyricFile);
                mLyricRows.postValue(lyricRows);
            } else {
                mLyricRows.postValue(new ArrayList<>());
            }
        });
    }

    public void applyLyricFromNetWork(Music music) {
        RetrofitManager.searchMusic(music.getMusicName(), new Observer<SearchMusicResponse>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(SearchMusicResponse searchMusicResponse) {
                if (!searchMusicResponse.getData().isEmpty()) {
                    Artist artist = QueryExecutor.findArtist(music);
                    getSuitableLyric(music, artist, searchMusicResponse.getData());
                } else {
                    AndroidSchedulers.mainThread().scheduleDirect(() -> ToastUtil.showToast(MainApplication.getForegroundContext(), R.string.no_lyric_available));
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                ToastUtil.showToast(MainApplication.getForegroundContext(), R.string.download_failed);
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @WorkerThread
    private void getSuitableLyric(Music targetMusic, Artist targetArtist, List<OnlineMusic> onlineMusicList) {
        String targetMusicName = targetMusic.getMusicName();
        String targetArtistName = targetArtist.getArtistName();
        double maxConfidence = 0;
        OnlineMusic maxMatchMusic = null;
        for (OnlineMusic onlineMusic : onlineMusicList) {
            String musicName = onlineMusic.getName();
            String artistName = onlineMusic.getSinger();
            double confidence = MinEditDistance.SimilarDegree(targetMusicName, musicName) + MinEditDistance.SimilarDegree(targetArtistName, artistName);
            if (confidence > maxConfidence) {
                maxConfidence = confidence;
                maxMatchMusic = onlineMusic;
            }
        }
        downloadLyric(targetMusic, maxMatchMusic.getLrc());
    }

    @WorkerThread
    private void downloadLyric(Music music, String lyricUrl) {
        if (lyricUrl != null) {
            ThreadPool.execute(() -> {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                BufferedWriter writer = null;
                File file = new File(MainApplication.getForegroundContext().getExternalFilesDir("/"), music.getMusicName());
                if (file.exists()) {
                    file.delete();
                }
                try {
                    URL url = new URL(lyricUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8 * 1000);
                    connection.setReadTimeout(8 * 1000);
                    InputStream in = connection.getInputStream();
                    writer = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                    reader = new BufferedReader(new InputStreamReader(in));
                    List<String> rows = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        rows.add(line);
                        writer.write(line);
                        writer.newLine();
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
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
                final int tip;
                if (mLyricRows.getValue() != null && !mLyricRows.getValue().isEmpty()) {
                    tip = R.string.download_success;
                } else {
                    tip = R.string.no_lyric_available;
                }
                AndroidSchedulers.mainThread().scheduleDirect(() -> ToastUtil.showToast(MainApplication.getForegroundContext(), tip));
                Lyric lyric = new Lyric(music.getMusicId(), file.getAbsolutePath());
                DBManager.getInstance().getDaoSession().getLyricDao().insertOrReplace(lyric);
            });
        } else {
            ToastUtil.showToast(MainApplication.getForegroundContext(), R.string.no_lyric_available);
        }
    }
}
