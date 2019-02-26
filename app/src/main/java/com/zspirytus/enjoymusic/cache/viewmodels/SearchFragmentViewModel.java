package com.zspirytus.enjoymusic.cache.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;

import com.zspirytus.enjoymusic.base.BaseActivity;
import com.zspirytus.enjoymusic.db.table.Album;
import com.zspirytus.enjoymusic.db.table.Artist;
import com.zspirytus.enjoymusic.db.table.Music;
import com.zspirytus.enjoymusic.entity.listitem.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentViewModel extends ViewModel {

    private MutableLiveData<List<SearchResult>> mSearchResultList;

    private List<Music> allMusicList;
    private List<Album> albumList;
    private List<Artist> artistList;

    public void init(BaseActivity parentActivity) {
        mSearchResultList = new MutableLiveData<>();
        MainActivityViewModel viewModel = ViewModelProviders.of(parentActivity).get(MainActivityViewModel.class);
        viewModel.getMusicList().observe(parentActivity, valuse -> allMusicList = valuse);
        viewModel.getAlbumList().observe(parentActivity, values -> albumList = values);
        viewModel.getArtistList().observe(parentActivity, values -> artistList = values);
    }

    public void applyToSearch(String key) {
        List<SearchResult> results = new ArrayList<>();
        SearchResult result = new SearchResult();

        List<SearchResult> musicResult = searchMusic(key);
        if (musicResult != null && !musicResult.isEmpty()) {
            result.setTitle(true);
            result.setTitle("曲目");
            results.add(result);
            results.addAll(musicResult);
        }

        List<SearchResult> albumResult = searchAlbum(key);
        if (albumResult != null && !albumResult.isEmpty()) {
            result = new SearchResult();
            result.setTitle(true);
            result.setTitle("专辑");
            results.add(result);
            results.addAll(albumResult);
        }

        List<SearchResult> artistResult = searchArtist(key);
        if (artistResult != null && !artistResult.isEmpty()) {
            result = new SearchResult();
            result.setTitle(true);
            result.setTitle("艺术家");
            results.add(result);

            results.addAll(artistResult);
        }

        mSearchResultList.setValue(results);
    }

    public MutableLiveData<List<SearchResult>> getSearchResultList() {
        return mSearchResultList;
    }

    private List<SearchResult> searchMusic(String key) {
        List<SearchResult> results = new ArrayList<>();
        for (Music music : allMusicList) {
            if (music.getMusicName().contains(key)) {
                SearchResult result = new SearchResult();
                result.setMusic(true);
                result.setMusic(music);
                results.add(result);
            }
        }
        return results;
    }

    private List<SearchResult> searchAlbum(String key) {
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

    private List<SearchResult> searchArtist(String key) {
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
