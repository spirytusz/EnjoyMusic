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
        String SET_DFAB_LISTENER = "set dFab listener";
        String MUSIC_NAME_SET = "music name set";
        String SHOW_MUSIC_PLAY_FRAGMENT = "show music play fragment";
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
        String ALL = "All";
        String ALBUM = "Album";
        String ARTIST = "Artist";
    }
}
