package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;

import com.zspirytus.enjoymusic.R;
import com.zspirytus.enjoymusic.base.BaseActivity;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.listitem.SearchResult;
import com.zspirytus.enjoymusic.global.MainApplication;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentViewModel extends ViewModel {

    private MutableLiveData<List<SearchResult>> mSearchResultList;

    public void init() {
        mSearchResultList = new MutableLiveData<>();
    }

    public void applyToSearch(BaseActivity activity, String key) {
        Resources resources = MainApplication.getForegroundContext().getResources();
        MainActivityViewModel viewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
        List<SearchResult> results = new ArrayList<>();
        SearchResult result = new SearchResult();

        List<SearchResult> musicResult = searchMusic(viewModel.getMusicList().getValue(), key);
        if (musicResult != null && !musicResult.isEmpty()) {
            result.setTitle(true);
            result.setTitle(resources.getString(R.string.all_music));
            results.add(result);
            results.addAll(musicResult);
        }

        List<SearchResult> albumResult = searchAlbum(viewModel.getAlbumList().getValue(), key);
        if (albumResult != null && !albumResult.isEmpty()) {
            result = new SearchResult();
            result.setDividerLine(true);
            results.add(result);

            result = new SearchResult();
            result.setTitle(true);
            result.setTitle(resources.getString(R.string.album));
            results.add(result);
            results.addAll(albumResult);
        }

        List<SearchResult> artistResult = searchArtist(viewModel.getArtistList().getValue(), key);
        if (artistResult != null && !artistResult.isEmpty()) {
            result = new SearchResult();
            result.setDividerLine(true);
            results.add(result);

            result = new SearchResult();
            result.setTitle(true);
            result.setTitle(resources.getString(R.string.artist));
            results.add(result);
            results.addAll(artistResult);
        }

        mSearchResultList.setValue(results);
    }

    public MutableLiveData<List<SearchResult>> getSearchResultList() {
        return mSearchResultList;
    }

    private List<SearchResult> searchMusic(List<Music> musicList, String key) {
        List<SearchResult> results = new ArrayList<>();
        for (Music music : musicList) {
            if (music.getMusicName().contains(key)) {
                SearchResult result = new SearchResult();
                result.setMusic(true);
                result.setMusic(music);
                results.add(result);
            }
        }
        return results;
    }

    private List<SearchResult> searchAlbum(List<Album> albumList, String key) {
        List<SearchResult> results = new ArrayList<>();
        for (Album album : albumList) {
            if (album.getAlbumName().contains(key)) {
                SearchResult result = new SearchResult();
                result.setAlbum(true);
                result.setAlbum(album);
                results.add(result);
            }
        }
        return results;
    }

    private List<SearchResult> searchArtist(List<Artist> artistList, String key) {
        List<SearchResult> results = new ArrayList<>();
        for (Artist artist : artistList) {
            if (artist.getArtistName().contains(key)) {
                SearchResult result = new SearchResult();
                result.setArtist(true);
                result.setArtist(artist);
                results.add(result);
            }
        }
        return results;
    }
}
