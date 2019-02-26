package com.zspirytus.enjoymusic.engine;

/**
 * Lrc文件协议(From https://zh.wikipedia.org/wiki/LRC%E6%A0%BC%E5%BC%8F):
 * 简易格式:
 * [mm:ss.xx] 第一行歌词
 * [mm:ss.xx] 第二行歌词
 * ...
 * [mm:ss.xx] 最后一行歌词
 * <p>
 * mm -> 分钟
 * ss -> 秒钟
 * xx -> 百分之一秒
 * <p>
 * ID标签:
 * [al:本歌所在的唱片集]
 * [ar:演出者-歌手]
 * [au:歌詞作者-作曲家]
 * [by:此LRC文件的创建者]
 * [offset:+/- 以毫秒为单位加快或延後歌詞的播放]
 * [re:创建此LRC文件的播放器或编辑器]
 * [ti:歌词(歌曲)的标题]
 * [ve:程序的版本]
 * <p>
 * 增强格式:
 * [mm:ss.xx] <mm:ss.xx> 第一行第一个词 <mm:ss.xx> 第一行第二个词 <mm:ss.xx> ... 第一行最后一个词 <mm:ss.xx>
 * [mm:ss.xx] <mm:ss.xx> 第二行第一个词 <mm:ss.xx> 第二行第二个词 <mm:ss.xx> ... 第二行最后一个词 <mm:ss.xx>
 * ...
 * [mm:ss.xx] <mm:ss.xx> 最后一行第一个词 <mm:ss.xx> 最后一行第二个词 <mm:ss.xx> ...  最后一行最后一个词 <mm:ss.xx>
 */

import android.util.SparseIntArray;

import com.zspirytus.enjoymusic.entity.LyricRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricLoader {

    private static class Singleton {
        static LyricLoader INSTANCE = new LyricLoader();
    }

    private LyricLoader() {
    }

    public static LyricLoader getInstance() {
        return Singleton.INSTANCE;
    }

    public List<LyricRow> load(File file) {
        List<String> rows = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                rows.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return load(rows);
    }

    public List<LyricRow> load(List<String> rows) {
        SparseIntArray indexMapper = new SparseIntArray();
        List<LyricRow> lyricRows = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2}|\\d{2}:\\d{2})\\](.*)");
        for (String row : rows) {
            Matcher matcher = pattern.matcher(row);
            if (matcher.find()) {
                // 获取时间
                String time = matcher.group(1);
                // 获取歌词
                String lyric = matcher.group(matcher.groupCount());
                if (lyric.equals("//")) {
                    continue;
                }

                int index = indexMapper.get(time.hashCode(), -1);
                if (index != -1) {
                    String previousLyric = lyricRows.get(index).getText();
                    String newLyric = previousLyric + "\n" + lyric;
                    lyricRows.get(index).setText(newLyric);
                } else {
                    LyricRow lyricRow = new LyricRow();
                    lyricRow.setTime(time);
                    lyricRow.setText(lyric);
                    lyricRows.add(lyricRow);
                    indexMapper.put(time.hashCode(), lyricRows.size() - 1);
                }
            }
        }
        Collections.sort(lyricRows);
        return lyricRows;
    }
}
