package com.zspirytus.enjoymusic.factory;

import com.zspirytus.enjoymusic.cache.constant.Constant;
import com.zspirytus.enjoymusic.engine.ForegroundMusicController;
import com.zspirytus.enjoymusic.entity.Album;
import com.zspirytus.enjoymusic.entity.Artist;
import com.zspirytus.enjoymusic.entity.HomePageChildRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.HomePageRecyclerViewItem;
import com.zspirytus.enjoymusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by ZSpirytus on 2018/9/17.
 */

public class ObservableFactory {

    public static Observable<Music> getMusicObservable() {
        return Observable.create(new ObservableOnSubscribe<Music>() {
            @Override
            public void subscribe(ObservableEmitter<Music> emitter) throws Exception {
                List<Music> musicList = ForegroundMusicController.getInstance().getAllMusicList();
                for (Music music : musicList) {
                    emitter.onNext(music);
                }
                emitter.onComplete();
            }
        });
    }

    public static Observable<Album> getAlbumObservable() {
        return getMusicObservable().map(new Function<Music, Album>() {
            @Override
            public Album apply(Music music) throws Exception {
                return new Album(music.getMusicAlbumName(), music.getMusicThumbAlbumCoverPath(), music.getMusicArtist());
            }
        });
    }

    public static Observable<Artist> getArtistObservable() {
        return getMusicObservable().map(new Function<Music, Artist>() {
            @Override
            public Artist apply(Music music) throws Exception {
                return new Artist(music.getMusicArtist());
            }
        });
    }

    public static Observable<List<HomePageRecyclerViewItem>> getHomePageRecyclerViewItemsObservable() {
        return Observable.create(new ObservableOnSubscribe<List<HomePageRecyclerViewItem>>() {
            @Override
            public void subscribe(ObservableEmitter<List<HomePageRecyclerViewItem>> emitter) throws Exception {
                List<Music> musicList = ForegroundMusicController.getInstance().getAllMusicList();
                List<Album> albumList = ForegroundMusicController.getInstance().getAlbumList();
                List<Artist> artistList = ForegroundMusicController.getInstance().getArtistList();
                List<HomePageChildRecyclerViewItem> childOfMusic = new ArrayList<>();
                List<HomePageChildRecyclerViewItem> childOfAlbum = new ArrayList<>();
                List<HomePageChildRecyclerViewItem> childOfArtist = new ArrayList<>();
                for (Music music : musicList) {
                    childOfMusic.add(new HomePageChildRecyclerViewItem(music.getMusicThumbAlbumCoverPath(), music.getMusicName(), music.getMusicArtist()));
                }
                for (Album album : albumList) {
                    childOfAlbum.add(new HomePageChildRecyclerViewItem(album.getAlbumCoverPath(), album.getAlbumName(), album.getArtist()));
                }
                for (Artist artist : artistList) {
                    childOfArtist.add(new HomePageChildRecyclerViewItem(null, artist.getArtistName(), artist.getMusicCount() + " 首曲目"));
                }
                List<HomePageRecyclerViewItem> recyclerViewItems = new ArrayList<>();
                recyclerViewItems.add(new HomePageRecyclerViewItem(Constant.HomePageTabTitle.ALL, childOfMusic));
                recyclerViewItems.add(new HomePageRecyclerViewItem(Constant.HomePageTabTitle.ALBUM, childOfAlbum));
                recyclerViewItems.add(new HomePageRecyclerViewItem(Constant.HomePageTabTitle.ARTIST, childOfArtist));
                emitter.onNext(recyclerViewItems);
                emitter.onComplete();
            }
        });
    }

    public static Observable<Music> getMusicObservableConverterByTypeAndKey(final String key, int type) {
        switch (type) {
            case 0:
                return ObservableFactory.getMusicObservable();
            case 1:
                return ObservableFactory.getMusicObservable()
                        .filter(new Predicate<Music>() {
                            @Override
                            public boolean test(Music music) throws Exception {
                                return music.getMusicAlbumName().equals(key);
                            }
                        });
            case 2:
                return ObservableFactory.getMusicObservable()
                        .filter(new Predicate<Music>() {
                            @Override
                            public boolean test(Music music) throws Exception {
                                return music.getMusicArtist().equals(key);
                            }
                        });
        }
        return null;
    }
}
