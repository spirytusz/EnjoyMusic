package com.zspirytus.enjoymusic.entity;

import android.support.annotation.NonNull;

import java.util.TreeMap;

public class LyricRow implements Comparable<LyricRow> {

    private String time;
    private int timeIntValue;
    private String text;
    private TreeMap<Integer, String> lyricRowTime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
        setTimeIntValue(time);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TreeMap<Integer, String> getLyricRowTime() {
        return lyricRowTime;
    }

    public void putLyricRowTime(Integer index, String subLyricRow) {
        if (lyricRowTime == null) {
            lyricRowTime = new TreeMap<>();
        }
        lyricRowTime.put(index, subLyricRow);
    }

    private void setTimeIntValue(String time) {
        String[] strings = time.split(":");
        if (strings.length == 2) {
            timeIntValue += Integer.parseInt(strings[0]) * 60 * 1000;
            timeIntValue += (int) (Float.parseFloat(strings[1]) * 1000);
        }
    }

    private int getTimeIntValue() {
        return timeIntValue;
    }

    @Override
    public String toString() {
        if (time == null) {
            return text;
        } else if (lyricRowTime == null) {
            return "[" + time + "]" + text;
        } else {
            return "[" + time + "]" + text + "\tlyricRowTime = " + lyricRowTime;
        }
    }

    @Override
    public int compareTo(@NonNull LyricRow o) {
        return timeIntValue - o.getTimeIntValue();
    }
}
