package com.zspirytus.enjoymusic.view.fragment;

/**
 * Created by ZSpirytus on 2018/9/14.
 */

public class FragmentFactory {

    private static final FragmentFactory INSTANCE = new FragmentFactory();

    private HomePageFragment mHomePageFragment;
    private MusicCategoryFragment mMusicCategoryFragment;
    private SettingsFragment mSettingsFragment;
    private AboutFragment mAboutFragment;
    private MusicPlayFragment mMusicPlayFragment;
    private AllMusicListFragment mAllMusicListFragment;
    private AlbumMusicListFragment mAlbumMusicListFragment;
    private ArtistMusicListFragment mArtistMusicListFragment;

    private FragmentFactory() {

    }

    public static FragmentFactory getInstance() {
        return INSTANCE;
    }

    public <T extends BaseFragment> T create(Class<T> tClass) {
        if (tClass == HomePageFragment.class) {
            if (mHomePageFragment == null) {
                mHomePageFragment = HomePageFragment.getInstance();
            }
            return (T) mHomePageFragment;
        } else if (tClass == MusicCategoryFragment.class) {
            if (mMusicCategoryFragment == null) {
                mMusicCategoryFragment = MusicCategoryFragment.getInstance();
            }
            return (T) mMusicCategoryFragment;
        } else if (tClass == SettingsFragment.class) {
            if (mSettingsFragment == null) {
                mSettingsFragment = SettingsFragment.getInstance();
            }
            return (T) mSettingsFragment;
        } else if (tClass == AboutFragment.class) {
            if (mAboutFragment == null) {
                mAboutFragment = AboutFragment.getInstance();
            }
            return (T) mAboutFragment;
        } else if (tClass == MusicPlayFragment.class) {
            if (mMusicPlayFragment == null) {
                mMusicPlayFragment = MusicPlayFragment.getInstance();
            }
            return (T) mMusicPlayFragment;
        } else if (tClass == AllMusicListFragment.class) {
            if (mAllMusicListFragment == null) {
                mAllMusicListFragment = AllMusicListFragment.getInstance();
            }
            return (T) mAllMusicListFragment;
        } else if (tClass == AlbumMusicListFragment.class) {
            if (mAlbumMusicListFragment == null) {
                mAlbumMusicListFragment = AlbumMusicListFragment.getInstance();
            }
            return (T) mAlbumMusicListFragment;
        } else if (tClass == ArtistMusicListFragment.class) {
            if (mArtistMusicListFragment == null) {
                mArtistMusicListFragment = ArtistMusicListFragment.getInstance();
            }
            return (T) mArtistMusicListFragment;
        }
        return null;
    }
}
