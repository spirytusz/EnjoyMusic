package com.zspirytus.enjoymusic.cache.constant;

/**
 * Created by ZSpirytus on 2018/9/7.
 */

public interface Constant {

    interface EventBusTag {
        String PLAY = "play";
        String PAUSE = "pause";
        String STOP = "stop";
        String SEEK_TO = "seek to";
        String MUSIC_NAME_SET = "music name set";
        String SHOW_CAST_FRAGMENT = "show cast fragment";
        String START_MAIN_ACTIVITY = "start MainActivity";
        String SET_MAIN_ACTIVITY_TOOLBAR_TITLE = "set MainActivity toolbar title";
        String SET_DFAB_VISIBLE = "set DFab Visible";
    }

    interface AnimationProperty {
        String ALPHA = "alpha";
    }

    interface AnimationDuration {
        int SHORT_SHORT_DURATION = 145;
        int SHORT_DURATION = 382;
        int LONG_DURATION = 618;
    }

    interface StatusBarEvent {
        String ACTION_NAME = "com.zspirytus.enjoymusic.STATUS_BAR_ACTION";
        String EXTRA = "statusBarExtra";
        String PLAY_OR_PAUSE = "playOrPause";
        String NEXT = "next";
        String PREVIOUS = "previous";
        String SINGLE_CLICK = "singleClick";
    }

    interface HomePageTabTitle {
        String ALL = "曲目";
        String ALBUM = "专辑";
        String ARTIST = "艺术家";
    }

    interface RecyclerViewItemType {
        int ALL_MUSIC_ITEM_TYPE = 1;
        int ARTIST_MUSIC_ITEM_TYPE = 2;
    }
}
