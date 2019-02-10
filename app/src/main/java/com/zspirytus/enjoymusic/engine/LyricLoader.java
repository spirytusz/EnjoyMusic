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

import com.zspirytus.enjoymusic.entity.LyricRow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LyricLoader {

    public List<LyricRow> load(File file) {
        StringBuilder resultBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
                resultBuilder.append('\n');
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
        return load(resultBuilder.toString());
    }

    public List<LyricRow> load(String text) {
        List<LyricRow> lyricRows = new ArrayList<>();
        String[] tagName = new String[]{
                "",
                "专辑: ",
                "歌曲: ",
                "艺术家: ",
                "作曲家: ",
                "歌词作者: ",
        };
        Pattern[] patterns = new Pattern[]{
                Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2}|\\d{2}:\\d{2})\\](.*)"),
                // 时间, 歌词
                Pattern.compile("\\[al:(.*)]"),
                // 专辑
                Pattern.compile("\\[ti:(.*)]"),
                // 歌名
                Pattern.compile("\\[ar:(.*)]"),
                // 艺术家
                Pattern.compile("\\[au:(.*)]"),
                // 作曲家
                Pattern.compile("\\[by:(.*)]")
                // 歌词作者
        };
        String[] rows = text.split("\n");
        for (String row : rows) {
            for (int i = 0; i < patterns.length; i++) {
                Matcher matcher = patterns[i].matcher(row);
                if (matcher.find()) {
                    LyricRow lyricRow = new LyricRow();
                    if (i == 0) {
                        // 获取时间
                        lyricRow.setTime(matcher.group(1));
                        // 获取歌词
                        String lyric = matcher.group(matcher.groupCount());
                        String reg = "<(\\d{2}:\\d{2}\\.\\d{2}|\\d{2}:\\d{2})>(.+?)";
                        Matcher advanceLyricMatcher = Pattern.compile(reg).matcher(lyric);
                        if (advanceLyricMatcher.find()) {
                            // 增强格式
                            parseAdvanceLyric(lyricRow, advanceLyricMatcher);
                        } else {
                            // 简易格式
                            lyricRow.setText(lyric);
                        }
                    } else {
                        // 获取标签
                        lyricRow.setText(tagName[i] + matcher.group(1));
                    }
                    lyricRows.add(lyricRow);
                    break;
                }
            }
        }
        return lyricRows;
    }

    private void parseAdvanceLyric(LyricRow row, Matcher matcher) {
        int offset = 0;
        StringBuilder textBuilder = new StringBuilder();
        matcher.reset();
        while (matcher.find()) {
            String time = matcher.group(1);
            String subRow = matcher.group(2);
            row.putLyricRowTime(offset, time);
            offset += subRow.length();
            textBuilder.append(subRow);
        }
        row.setText(textBuilder.toString());
    }

    // TODO: 10/02/2019 add url support.
    /*@WorkerThread
    public List<LyricRow> load(String url, int timeout) {
        String html = getHtml(url, timeout);
        return null;
    }

    private String getHtml(String url, int timeout) {
        StringBuilder responseTextBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL parsedUrl = new URL(url);
            connection = (HttpURLConnection) parsedUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                responseTextBuilder.append(line);
                responseTextBuilder.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return responseTextBuilder.toString();
    }*/
}
