package com.zspirytus.enjoymusic.cache.finalvalue;

/**
 * Created by ZSpirytus on 2018/9/7.
 */

public interface FinalValue {

    interface EventBusTag {
        String PLAY = "play";
        String PAUSE = "pause";
        String STOP = "stop";
        String SEEK_TO = "seek to";
        String SET_DFAB_LISTENER = "set dFab listener";
        String MUSIC_NAME_SET = "music name set";
        String SHOW_MUSIC_PLAY_FRAGMENT = "show music play fragment";
    }

    interface AnimationProperty {
        String ALPHA = "alpha";
    }

    interface StatusBarEvent {
        String ACTION_NAME = "com.zspirytus.enjoymusic.STATUS_BAR_ACTION";
        String EXTRA = "statusBarExtra";
        String PLAY_OR_PAUSE = "playOrPause";
        String NEXT = "next";
        String PREVIOUS = "previous";
        String SINGLE_CLICK = "singleClick";
    }
}
