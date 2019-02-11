package com.zspirytus.enjoymusic.engine;

import com.zspirytus.enjoymusic.entity.LyricRow;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LyricLoaderTest {

    @Test
    public void load() {
        LyricLoader loader = new LyricLoader();
        String testLyric = "[ti:Let's Twist Again]\n" +
                "[ar:Chubby Checker oppure  Beatles The]\n" +
                "[au:Written by Kal Mann / Dave Appell 1961]\n" +
                "[al:Hits Of The 60's - Vol. 2 – Oldies]\n" +
                "[00:12]Naku Penda Piya-Naku Taka Piya-Mpenziwe\n" +
                "[00:15.30]Some more lyrics ...\n" +
                "[00:18.00]<00:18.10>哈<00:18.20>呵<00:18.30>嘻<00:18.40>嘿<00:18.50>";
        String[] rows = testLyric.split("\n");
        List<String> wrapRows = Arrays.asList(rows);
        List<LyricRow> lyricRows = loader.load(wrapRows);
        for (LyricRow row : lyricRows) {
            System.out.println(row.getTime());
        }
    }
}