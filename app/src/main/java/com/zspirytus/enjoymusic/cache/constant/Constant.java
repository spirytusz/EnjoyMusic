package com.zspirytus.enjoymusic.cache.constant;

import java.util.ArrayList;
import java.util.List;

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
        int AUDIO_EFFECT = 0x81;
        int MUSIC_META_DATA_UPDATOR = 0x82;
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
        String folderSortedMusicListFragment = "FolderListFragment";
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

    class MenuTexts {
        public static List<String> menuTexts;
        public static List<String> albumMenuTexts;
        public static List<String> artistMenuTexts;
        public static List<String> folderMenuTexts;
        public static List<String> songListMenuTexts;

        static {
            menuTexts = new ArrayList<>();
            menuTexts.add("添加到播放队列");
            menuTexts.add("从本机中删除");
            menuTexts.add("专辑");
            menuTexts.add("艺术家");
            menuTexts.add("编辑音乐信息");

            albumMenuTexts = new ArrayList<>();
            albumMenuTexts.add("添加到播放队列");
            albumMenuTexts.add("新建歌单");
            albumMenuTexts.add("曲目");
            albumMenuTexts.add("艺术家");

            artistMenuTexts = new ArrayList<>();
            artistMenuTexts.add("添加到播放队列");
            artistMenuTexts.add("新建歌单");
            artistMenuTexts.add("曲目");
            artistMenuTexts.add("专辑");

            folderMenuTexts = new ArrayList<>();
            folderMenuTexts.add("添加到播放队列");
            folderMenuTexts.add("新建歌单");

            songListMenuTexts = new ArrayList<>();
            songListMenuTexts.add("添加到播放队列");
        }
    }
}
