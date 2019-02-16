package com.zspirytus.enjoymusic.cache.constant;

/**
 * Created by ZSpirytus on 2018/9/7.
 */

public interface Constant {

    interface BinderCode {
        int MUSIC_CONTROL = 0x00;
        int MUSIC_PROGRESS_CONTROL = 0x01;
        int PLAY_PROGRESS_CHANGE_OBSERVER = 0x02;
        int PLAY_STATE_CHANGE_OBSERVER = 0x04;
        int PLAY_MUSIC_CHANGE_OBSERVER = 0x08;
        int GET_MUSIC_LIST = 0x10;
        int SET_PLAY_LIST = 0x20;
        int BACKGROUND_EVENT_PROCESSOR = 0x40;
        int PLAY_LIST_OBSERVER = 0x80;
        int EQUALIZER_HELPER = 0x100;
    }

    interface EventBusTag {
        String SHOW_CAST_FRAGMENT = "show cast fragment";
        String OPEN_DRAWER = "openDrawer";
    }

    interface AnimationProperty {
        String ALPHA = "alpha";
        String TRANSLATE_Y = "translationY";
    }

    interface AnimationDuration {
        int SHORT_SHORT_DURATION = 145;
        int SHORT_DURATION = 382;
        int LONG_DURATION = 618;
    }

    interface NotificationEvent {
        String ACTION_NAME = "com.zspirytus.enjoymusic.STATUS_BAR_ACTION";
        String EXTRA = "statusBarExtra";
        String PLAY = "play";
        String PAUSE = "pause";
        String NEXT = "next";
        String PREVIOUS = "previous";
        String SINGLE_CLICK = "singleClick";
    }

    interface FragmentName {
        String folderSortedMusicListFragment = "FolderSortedMusicListFragment";
        String aboutFragmentName = "AboutFragment";
        String albumMusicListFragmentName = "AlbumMusicListFragment";
        String allMusicListFragmentName = "AllMusicListFragment";
        String artistMusicListFragmentName = "ArtistMusicListFragment";
        String homePageFragmentName = "HomePageFragment";
        String musicCategoryFragmentName = "MusicCategoryFragment";
        String musicPlayFragmentName = "MusicPlayFragment";
        String playListFragmentName = "PlayListFragment";
        String settingFragmentName = "SettingsFragment";
    }

    interface PlayMode {
        int RANDOM = 0;
        int SINGLE_LOOP = 1;
        int LIST_LOOP = 2;
    }
}
