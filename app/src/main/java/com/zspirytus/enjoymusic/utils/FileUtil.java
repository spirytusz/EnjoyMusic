package com.zspirytus.enjoymusic.utils;

import java.io.File;

public class FileUtil {

    private FileUtil() {
        throw new AssertionError();
    }

    /**
     * Get Folder and Folder its Directory.
     *
     * @param path
     * @return String array, arr[0] == folder, arr[1] == folder its directory, arr[2] == folder its absolute path.
     */
    public static String[] getFolderNameAndFolderDir(String path) {
        File file = new File(path);
        File folder = file.getParentFile();
        String folderName = folder.getName();
        String folderDir = folder.getParentFile().getAbsolutePath();
        return new String[]{folderName, folderDir, folder.getAbsolutePath()};
    }
}
