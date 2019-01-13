package com.zspirytus.enjoymusic.utils;

import java.io.File;

public class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }

    public static String getParent(String path) {
        return new File(path).getParent();
    }

    public static String[] getParentFileNameAndDir(String path) {
        File parent = new File(path).getParentFile();
        return new String[]{
                parent.getName(),
                parent.getParentFile().getPath()
        };
    }
}
