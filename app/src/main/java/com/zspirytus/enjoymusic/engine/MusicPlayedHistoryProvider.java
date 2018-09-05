package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.entity.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZSpirytus on 2018/9/5.
 */

public class MusicPlayedHistoryProvider {

    private static final MusicPlayedHistoryProvider INSTANCE = new MusicPlayedHistoryProvider();

    private List<Music> playedHistory;

    private MusicPlayedHistoryProvider() {
        playedHistory = new ArrayList<>();
    }

    public static MusicPlayedHistoryProvider getInstance() {
        return INSTANCE;
    }

    public void put(Music music) {
        playedHistory.add(music);
    }
}
